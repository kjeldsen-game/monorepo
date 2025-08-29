import { Play } from 'modules/match/types/MatchResponses';
import { useEffect, useState } from 'react';

export const useEventBuilder = (plays: Play[]) => {
  const [loading, setLoading] = useState<boolean>(true);
  const [possesions, setPossesions] = useState<Play[][]>([]);
  const [highlights, setHighlights] = useState<Play[][]>([]);

  useEffect(() => {
    setLoading(true);
    let possesions: Play[][] = [];
    plays?.forEach((event: Play) => {
      let lastGroup = possesions.at(-1);

      if (
        lastGroup &&
        lastGroup[0].duel.initiator.id === event.duel.initiator.id
      ) {
        lastGroup.push(event);
      } else {
        possesions.push([event]);
      }
    });
    setPossesions(possesions);

    let highlights: Play[][] = plays
      ?.filter((play: Play) => play.action === 'SHOOT')
      .map((play: Play) => [play]);
    setHighlights(highlights);

    setLoading(false);
  }, [plays]);

  return {
    loading,
    possesions,
    highlights,
  };
};
