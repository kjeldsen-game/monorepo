export const formatPlayerSkills = (skill: any) => {
  const actual = skill?.PlayerSkills?.actual ?? 0;
  const potential = skill?.PlayerSkills?.potential ?? 0;
  return `${actual}/${potential}`;
};
