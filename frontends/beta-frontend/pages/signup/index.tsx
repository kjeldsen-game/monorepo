import { NextPageWithLayout } from '@/pages/_app';
import SignUpView from '@/shared/components/Auth/SignUpView';
import LayoutNoMenu from '@/shared/layout/LayoutNoMenu';

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
