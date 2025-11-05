import React, { useState, useEffect } from 'react';
import api from '../../services/api';

interface Task {
  id: number;
  title: string;
  description: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  dueDate: string;
}

const Tasks: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [filter, setFilter] = useState<string>('ALL');

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      const response = await api.get('/tasks');
      setTasks(response.data);
    } catch (error) {
      console.error('Erro ao carregar tarefas:', error);
    }
  };

  const updateTaskStatus = async (id: number, status: string) => {
    try {
      await api.patch(`/tasks/${id}/status`, { status });
      loadTasks();
    } catch (error) {
      console.error('Erro ao atualizar tarefa:', error);
    }
  };

  const deleteTask = async (id: number) => {
    if (window.confirm('Tem certeza que deseja excluir esta tarefa?')) {
      try {
        await api.delete(`/tasks/${id}`);
        loadTasks();
      } catch (error) {
        console.error('Erro ao excluir tarefa:', error);
      }
    }
  };

  const filteredTasks = tasks.filter(task => {
    if (filter === 'ALL') return true;
    return task.status === filter;
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800';
      case 'COMPLETED': return 'bg-green-100 text-green-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'HIGH': return 'text-red-600';
      case 'MEDIUM': return 'text-yellow-600';
      case 'LOW': return 'text-green-600';
      default: return 'text-gray-600';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Minhas Tarefas</h1>
          <div className="flex gap-4">
            <select
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
              className="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="ALL">Todas</option>
              <option value="PENDING">Pendentes</option>
              <option value="IN_PROGRESS">Em Andamento</option>
              <option value="COMPLETED">Concluídas</option>
            </select>
          </div>
        </div>

        <div className="space-y-4">
          {filteredTasks.map((task) => (
            <div key={task.id} className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition">
              <div className="flex justify-between items-start">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="text-xl font-semibold text-gray-900">{task.title}</h3>
                    <span className={`px-3 py-1 rounded-full text-sm ${getStatusColor(task.status)}`}>
                      {task.status === 'PENDING' && 'Pendente'}
                      {task.status === 'IN_PROGRESS' && 'Em Andamento'}
                      {task.status === 'COMPLETED' && 'Concluída'}
                    </span>
                    <span className={`font-semibold ${getPriorityColor(task.priority)}`}>
                      {task.priority === 'HIGH' && '🔴 Alta'}
                      {task.priority === 'MEDIUM' && '🟡 Média'}
                      {task.priority === 'LOW' && '🟢 Baixa'}
                    </span>
                  </div>
                  <p className="text-gray-600 mb-2">{task.description}</p>
                  <p className="text-sm text-gray-500">
                    Prazo: {new Date(task.dueDate).toLocaleDateString('pt-BR')}
                  </p>
                </div>
                <div className="flex gap-2">
                  {task.status !== 'COMPLETED' && (
                    <button
                      onClick={() => updateTaskStatus(task.id, task.status === 'PENDING' ? 'IN_PROGRESS' : 'COMPLETED')}
                      className="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition"
                    >
                      ✓
                    </button>
                  )}
                  <button
                    onClick={() => deleteTask(task.id)}
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
                  >
                    🗑️
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {filteredTasks.length === 0 && (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">Nenhuma tarefa encontrada.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Tasks;
