import React, { useEffect, useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import PomodoroTimer from '../../components/timer/PomodoroTimer';
import api from '../../services/api';

interface WeeklyStats {
  totalMinutes: number;
  totalHours: number;
  totalPomodoros: number;
  sessionsCount: number;
}

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<WeeklyStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchWeeklyStats();
  }, []);

  const fetchWeeklyStats = async () => {
    try {
      const response = await api.get('/sessions/weekly-stats');
      setStats(response.data);
    } catch (error) {
      console.error('Erro ao buscar estatísticas:', error);
    } finally {
      setLoading(false);
    }
  };

  const getNextLevel = () => {
    if (!user) return 0;
    return user.level * 100;
  };

  const getProgressPercentage = () => {
    if (!user) return 0;
    return (user.experience / getNextLevel()) * 100;
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-4xl font-bold text-gray-800 mb-8">
          Bem-vindo, {user?.username}! 👋
        </h1>

        {/* Gamificação */}
        <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-2xl font-bold text-gray-800">Nível {user?.level}</h2>
              <p className="text-gray-600">
                {user?.experience} / {getNextLevel()} XP
              </p>
            </div>
            <div className="text-5xl">🏆</div>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-4">
            <div
              className="bg-blue-600 h-4 rounded-full transition-all duration-300"
              style={{ width: `${getProgressPercentage()}%` }}
            ></div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
          {/* Estatísticas Semanais */}
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">
              📊 Estatísticas da Semana
            </h2>
            {loading ? (
              <p>Carregando...</p>
            ) : stats ? (
              <div className="space-y-4">
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Tempo Total:</span>
                  <span className="text-2xl font-bold text-blue-600">
                    {stats.totalHours.toFixed(1)}h
                  </span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Pomodoros:</span>
                  <span className="text-xl font-semibold">{stats.totalPomodoros}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-gray-600">Sessões:</span>
                  <span className="text-xl font-semibold">{stats.sessionsCount}</span>
                </div>
              </div>
            ) : (
              <p className="text-gray-500">Nenhuma sessão registrada esta semana.</p>
            )}
          </div>

          {/* Timer Pomodoro */}
          <PomodoroTimer onComplete={() => fetchWeeklyStats()} />
        </div>

        {/* Cards de Ações Rápidas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition duration-200 cursor-pointer">
            <div className="text-4xl mb-4">📚</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Matérias</h3>
            <p className="text-gray-600">Gerencie suas matérias e tópicos</p>
          </div>

          <div className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition duration-200 cursor-pointer">
            <div className="text-4xl mb-4">✅</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Tarefas</h3>
            <p className="text-gray-600">Organize suas tarefas de estudo</p>
          </div>

          <div className="bg-white rounded-lg shadow-lg p-6 hover:shadow-xl transition duration-200 cursor-pointer">
            <div className="text-4xl mb-4">📝</div>
            <h3 className="text-xl font-bold text-gray-800 mb-2">Anotações</h3>
            <p className="text-gray-600">Acesse suas anotações e arquivos</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
