import { NextPageWithLayout } from '@/pages/_app';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';
import ResetPasswordView from 'modules/auth/components/reset-password/ResetPasswordView';

const ResetPasswordPage: NextPageWithLayout = () => {
    return (
        <ResetPasswordView />
    );
};

ResetPasswordPage.getLayout = (page) => (
    <LayoutNoMenu>
        {page}
    </LayoutNoMenu>
);

export default ResetPasswordPage;