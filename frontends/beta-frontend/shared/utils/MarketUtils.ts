import { FormValues, SkillRanges } from '../components/Market/MarketFilter';
import { PlayerSkillShortcuts } from '../models/PlayerSkill';

const formatSkills = (
    skillRanges: SkillRanges,
    minLabel: string = 'from',
    maxLabel: string = 'to',
): string[] => {
    return Object.entries(skillRanges || {}).reduce<string[]>(
        (acc, [skillKey, skillValue]) => {
            const fullSkillName = PlayerSkillShortcuts[skillKey];
            if (
                fullSkillName &&
                (skillValue[minLabel] || skillValue[maxLabel])
            ) {
                acc.push(
                    `${fullSkillName}:${skillValue[minLabel] || ''}:${skillValue[maxLabel] || ''}`,
                );
            }
            return acc;
        },
        [],
    );
};

export const createAuctionQueryFilter = (formValues: FormValues) => {
    const params = new URLSearchParams();

    if (formValues.position) {
        params.set('position', formValues.position);
    }

    const actualSkills = formatSkills(formValues.skillRanges);
    if (actualSkills.length > 0) {
        params.set('skills', actualSkills.join(','));
    }
    const potentialSkills = formatSkills(
        formValues.skillRanges,
        'potentialFrom',
        'potentialTo',
    );
    if (potentialSkills.length > 0) {
        params.set('potentialSkill', potentialSkills.join(','));
    }
    Object.entries(formValues.playerOffer || {}).reduce((acc, [key, value]) => {
        if (key === 'min' && value) acc.set('minBid', value);
        if (key === 'max' && value) acc.set('maxBid', value);
        return acc;
    }, params);

    Object.entries(formValues.playerAge || {}).reduce((acc, [key, value]) => {
        if (key === 'from' && value) acc.set('minAge', value);
        if (key === 'to' && value) acc.set('maxAge', value);
        return acc;
    }, params);

    return params.toString();
};

export function formatString(input: string) {
    return input
        .toLowerCase()
        .split('_')
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
}
