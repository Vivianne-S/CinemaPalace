import React from "react";

const SeatGrid = ({ seats, selectedSeats, onSeatClick }) => {
  return (
    <div className="flex flex-col items-center gap-2 mt-6">
      {Object.entries(seats).map(([row, rowSeats]) => (
        <div key={row} className="flex gap-2">
          {rowSeats.map((seat) => {
            const isSelected = selectedSeats.includes(seat.id);
            const seatColor = seat.status === "available"
              ? "bg-green-600 hover:bg-green-500"
              : seat.status === "reserved"
              ? "bg-yellow-600 cursor-not-allowed opacity-50"
              : "bg-red-800 cursor-not-allowed";

            return (
              <button
                key={seat.id}
                onClick={() => seat.status === "available" && onSeatClick(seat.id)}
                className={`w-8 h-8 rounded-lg ${seatColor} ${
                  isSelected ? "ring-2 ring-pink-500 scale-110" : ""
                } transition-transform duration-200`}
              />
            );
          })}
        </div>
      ))}
      <div className="mt-6 flex gap-4 text-sm text-gray-400">
        <div><span className="inline-block w-4 h-4 bg-green-600 rounded mr-2"></span>Available</div>
        <div><span className="inline-block w-4 h-4 bg-yellow-600 rounded mr-2"></span>Reserved</div>
        <div><span className="inline-block w-4 h-4 bg-red-800 rounded mr-2"></span>Booked</div>
      </div>
    </div>
  );
};

export default SeatGrid;