import { NextPageWithLayout } from '@/pages/_app';
import SignUpView from '@/shared/components/Auth/SignUpView';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';

const ForgetPasswordPage: NextPageWithLayout = () => {

    return (
        <SignUpView />
    );
};

ForgetPasswordPage.getLayout = (page) => (
    <LayoutNoMenu>
        {page}
    </LayoutNoMenu>
);

export default ForgetPasswordPage;