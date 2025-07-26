import PendingChallengesColumns from './PendingChallengesColumns';

describe('PendingChallengesColumns', () => {
    const mockAccept = jest.fn();
    const mockDecline = jest.fn();
    const ownTeamId = 'team-123';

    it('should return 3 columns', () => {
        const columns = PendingChallengesColumns(ownTeamId, mockAccept, mockDecline);
        expect(columns).toHaveLength(3);
    });

    it('should have correct field names in order', () => {
        const columns = PendingChallengesColumns(ownTeamId, mockAccept, mockDecline);

        expect(columns[0].field).toBe('Enemy');        // TeamNameColumn
        expect(columns[1].field).toBe('Date');         // DateTimeColumn
        expect(columns[2].field).toBe('acceptButton'); // Custom action column
    });

    it('should define renderHeader and renderCell for each column', () => {
        const columns = PendingChallengesColumns(ownTeamId, mockAccept, mockDecline);

        columns.forEach((col) => {
            expect(typeof col.renderHeader).toBe('function');
            expect(typeof col.renderCell).toBe('function');
        });
    });
});
