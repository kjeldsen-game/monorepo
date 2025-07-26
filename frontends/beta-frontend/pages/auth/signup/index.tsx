import { NextPageWithLayout } from '@/pages/_app';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';
import SignUpView from 'modules/auth/components/signup/SignUpView';

const SignUpPage: NextPageWithLayout = () => {

    return (
        <SignUpView />
    );
};

SignUpPage.getLayout = (page) => (
    <LayoutNoMenu>
        {page}
    </LayoutNoMenu>
);

export default SignUpPage;
