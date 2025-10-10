import PastChallengesColumns from '../PastChallengesColumns';

describe('PastChallengesColumns', () => {
    const mockOnReportSelect = jest.fn();

    it('should return 4 columns', () => {
        const columns = PastChallengesColumns(mockOnReportSelect);
        expect(columns).toHaveLength(4);
    });

    it('should include expected fields in correct order', () => {
        const columns = PastChallengesColumns(mockOnReportSelect);

        expect(columns[0].field).toBe('Home');        // TeamNameColumn
        expect(columns[1].field).toBe('Away');        // TeamNameColumn
        expect(columns[2].field).toBe('Date');        // DateTimeColumn
        expect(columns[3].field).toBe('reportButton'); // Custom column
    });

});
