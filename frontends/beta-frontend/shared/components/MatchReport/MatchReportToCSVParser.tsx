import React from 'react';

type DuelAction = {
  clock: number;
  homeScore: number;
  awayScore: number;
  action: string;
  duel: {
    initiator: { name: string };
    challenger: { name: string };
    receiver: { name: string };
    result: string;
  };
};

interface MatchReportToCSVParserProps {
  dataPass: any;
}

const MatchReportToCSVParser: React.FC<MatchReportToCSVParserProps> = ({
  dataPass,
}) => {
  //   console.log(dataPass);

  const data = dataPass;

  const handleDownload = () => {
    const flatData = data.map((item) => ({
      clock: item.clock,
      homeScore: item.homeScore,
      awayScore: item.awayScore,
      action: item.action,
      initiator: item.duel.initiator.name,
      valueInitiator: item.duel.initiatorStats.total,
      challenger: item.duel.challenger?.name,
      valueChallenger: item.duel.challengerStats.total,
      receiver: item.duel.receiver?.name,
      result: item.duel.result,
    }));

    const toCSV = (items: any[]) => {
      if (!items.length) return '';

      const headers = Object.keys(items[0]);
      const rows = [
        headers.join(','),
        ...items.map((obj) =>
          headers.map((key) => `"${obj[key] ?? ''}"`).join(','),
        ),
      ];

      return rows.join('\n');
    };

    const csv = toCSV(flatData);
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.download = 'match-report.csv';
    link.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div>
      <h1>MatchReportToCSVParser</h1>
      <button onClick={handleDownload}>Download CSV</button>
    </div>
  );
};

export default MatchReportToCSVParser;
