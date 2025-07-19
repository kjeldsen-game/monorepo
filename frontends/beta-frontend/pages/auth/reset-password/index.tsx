import { NextPageWithLayout } from '@/pages/_app';
import ResetPasswordView from '@/shared/components/Auth/ResetPasswordView';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';

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