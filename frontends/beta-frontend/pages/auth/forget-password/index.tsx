import { NextPageWithLayout } from '@/pages/_app';
import ForgetPasswordView from '@/shared/components/Auth/ForgetPasswordView';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';

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