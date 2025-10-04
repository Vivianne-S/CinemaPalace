import React, { useEffect, useState } from 'react'
import axios from 'axios'
import SeatGrid from '../components/SeatGrid.jsx'

export default function SeatSelectionView() {
  const [seats, setSeats] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [selectedSeats, setSelectedSeats] = useState([])
  const [showtimeId] = useState('cdd3506d-15c2-4988-b8b9-23df69f4ad11')

  useEffect(() => {
    async function fetchSeats() {
      try {
        console.log('🎬 Fetching seats from Ktor backend...')

        const apiUrl = `http://localhost:8080/seat-selection/${showtimeId}`
        const res = await axios.get(apiUrl, {
          timeout: 8000,
          headers: { 'Accept': 'application/json' }
        })

        if (res.data && Array.isArray(res.data)) {
          setSeats(res.data)
          setError(null)
          console.log(`✅ Loaded ${res.data.length} seats from Ktor`)
        } else {
          throw new Error(`Invalid data format from backend: ${typeof res.data}`)
        }
      } catch (err) {
        console.error('❌ Failed to load seats from Ktor:', err)
        setError(`Failed to load seats: ${err.message}`)
      } finally {
        setLoading(false)
      }
    }

    fetchSeats()
  }, [showtimeId])

  const handleSeatSelect = (seat) => {
    setSelectedSeats(prev => {
      const isAlreadySelected = prev.some(s => s.id === seat.id)
      return isAlreadySelected ? prev.filter(s => s.id !== seat.id) : [...prev, seat]
    })
  }

  const handleConfirm = async () => {
    try {
      const seatIds = selectedSeats.map(s => s.id)
      const res = await axios.post('http://localhost:8080/seat-selection/reserve', {
        showtimeId,
        seatIds
      })
      console.log('✅ Reservation successful:', res.data)
      alert('Seats reserved successfully!')
    } catch (err) {
      console.error('❌ Failed to reserve seats:', err)
      alert(`Failed to reserve: ${err.message}`)
    }
  }

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-96">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-400 mb-4"></div>
        <p className="text-gray-400 text-lg">Loading seats from Ktor backend...</p>
        <p className="text-gray-500 text-sm mt-2">Endpoint: /seat-selection/{showtimeId}</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-96 p-6">
        <div className="text-red-400 text-6xl mb-4">❌</div>
        <p className="text-red-400 text-center text-lg mb-4">{error}</p>

        <div className="text-gray-400 text-sm text-center max-w-md mb-6">
          <p className="mb-2 font-semibold">Ktor Backend Routes:</p>
          <ul className="list-disc list-inside text-left space-y-1">
            <li><code>/seat-selection/{showtimeId}</code> - Get seats</li>
            <li><code>/seat-selection/reserve</code> - Reserve seats</li>
            <li><code>/health</code> - Health check</li>
          </ul>
        </div>

        <button
          onClick={() => window.location.reload()}
          className="bg-yellow-500 hover:bg-yellow-600 text-gray-900 font-bold py-2 px-6 rounded-lg transition"
        >
          Retry Connection
        </button>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex flex-col items-center justify-start p-8 text-center">
      <h1 className="text-4xl md:text-5xl text-pink-500 drop-shadow-[0_0_15px_rgba(255,0,100,0.6)] mb-6">
        🎬 CinemaPalace
      </h1>

      <h2 className="text-xl text-gray-300 mb-4">Select Your Seats</h2>

      <div className="relative w-full max-w-3xl bg-gray-900/70 p-6 rounded-2xl shadow-lg border border-gray-800">
        <div className="w-full h-2 bg-gradient-to-r from-pink-500 via-red-600 to-yellow-500 rounded-full mb-6 shadow-[0_0_20px_rgba(255,0,100,0.4)]"></div>
        <p className="text-gray-400 mb-4">SCREEN</p>

        <SeatGrid seats={seats} selectedSeats={selectedSeats} onSeatClick={handleSeatSelect} />

        {selectedSeats.length > 0 && (
          <button
            onClick={handleConfirm}
            className="mt-6 bg-pink-600 hover:bg-pink-500 text-white px-6 py-2 rounded-lg shadow-md transition-all"
          >
            Confirm ({selectedSeats.length})
          </button>
        )}
      </div>
    </div>
  )
}