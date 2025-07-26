import { NextPageWithLayout } from '@/pages/_app';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';
import SignInView from 'modules/auth/components/signin/SignInView';

const SignInPage: NextPageWithLayout = () => {

    return (
        <SignInView />
    );
};

SignInPage.getLayout = (page) => (
    <LayoutNoMenu>
        {page}
    </LayoutNoMenu>
);

export default SignInPage;
