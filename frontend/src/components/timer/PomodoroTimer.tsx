import React, { useState, useEffect } from 'react';

interface PomodoroTimerProps {
  onComplete?: () => void;
}

const PomodoroTimer: React.FC<PomodoroTimerProps> = ({ onComplete }) => {
  const [minutes, setMinutes] = useState(25);
  const [seconds, setSeconds] = useState(0);
  const [isActive, setIsActive] = useState(false);
  const [isBreak, setIsBreak] = useState(false);
  const [pomodoroCount, setPomodoroCount] = useState(0);

  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;

    if (isActive) {
      interval = setInterval(() => {
        if (seconds === 0) {
          if (minutes === 0) {
            // Timer completado
            if (!isBreak) {
              setPomodoroCount((prev) => prev + 1);
              setIsBreak(true);
              setMinutes(5);
              setSeconds(0);
              if (onComplete) onComplete();
            } else {
              setIsBreak(false);
              setMinutes(25);
              setSeconds(0);
            }
            setIsActive(false);
          } else {
            setMinutes(minutes - 1);
            setSeconds(59);
          }
        } else {
          setSeconds(seconds - 1);
        }
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isActive, minutes, seconds, isBreak, onComplete]);

  const toggleTimer = () => {
    setIsActive(!isActive);
  };

  const resetTimer = () => {
    setIsActive(false);
    setIsBreak(false);
    setMinutes(25);
    setSeconds(0);
  };

  const formatTime = (min: number, sec: number) => {
    return `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-8 text-center">
      <h2 className="text-2xl font-bold mb-4">
        {isBreak ? '☕ Pausa' : '📚 Foco'}
      </h2>
      
      <div className="text-6xl font-mono font-bold mb-6 text-blue-600">
        {formatTime(minutes, seconds)}
      </div>

      <div className="mb-6">
        <span className="text-gray-600">Pomodoros completados: </span>
        <span className="font-bold text-lg">{pomodoroCount}</span>
      </div>

      <div className="flex gap-4 justify-center">
        <button
          onClick={toggleTimer}
          className={`px-6 py-3 rounded-lg font-semibold transition duration-200 ${
            isActive
              ? 'bg-yellow-500 hover:bg-yellow-600 text-white'
              : 'bg-green-500 hover:bg-green-600 text-white'
          }`}
        >
          {isActive ? 'Pausar' : 'Iniciar'}
        </button>

        <button
          onClick={resetTimer}
          className="px-6 py-3 bg-gray-500 hover:bg-gray-600 text-white rounded-lg font-semibold transition duration-200"
        >
          Resetar
        </button>
      </div>
    </div>
  );
};

export default PomodoroTimer;
