import { useSelector } from 'react-redux';
import { RootState } from '@/store/store';

export const useAuth = () => {
    const { user, isAuthenticated } = useSelector((state: RootState) => state.auth);
    return { user, isAuthenticated };
};