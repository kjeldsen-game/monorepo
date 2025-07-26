import { NextPageWithLayout } from '@/pages/_app';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';
import ForgetPasswordView from 'modules/auth/components/forget-password/ForgetPasswordView';

const ForgetPasswordPage: NextPageWithLayout = () => {
    return (
        <ForgetPasswordView />
    );
};

ForgetPasswordPage.getLayout = (page) => (
    <LayoutNoMenu>
        {page}
    </LayoutNoMenu>
);

export default ForgetPasswordPage;