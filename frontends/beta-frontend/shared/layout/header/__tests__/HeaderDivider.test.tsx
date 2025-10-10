import { render } from '@testing-library/react';
import HeaderDivider from '../HeaderDivider';
import '@testing-library/jest-dom';

test('renders HeaderDivider with correct styles', () => {
    const { getByRole } = render(<HeaderDivider />);
    const appBar = getByRole('banner');

    expect(appBar).toHaveStyle('background: #FF3F84');
    expect(appBar).toHaveStyle('height: 4px');
    expect(appBar).toHaveStyle('width: 100%');
});