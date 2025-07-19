import { NextPageWithLayout } from '@/pages/_app';
import SignInView from '@/shared/components/Auth/SignInView';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';

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
