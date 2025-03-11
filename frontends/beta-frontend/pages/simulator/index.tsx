import { useState } from 'react';
import {
  Box,
  TextField,
  FormControlLabel,
  Switch,
  Grid,
  Typography,
  Autocomplete,
  Paper,
} from '@mui/material';
import { PlayerSkill } from '@/shared/models/PlayerSkill';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { PlayerCategory } from '@/shared/models/PlayerCategory';
import { connectorAPI } from '@/libs/fetcher';
import { useSession } from 'next-auth/react';
import MarketButton from '@/shared/components/Market/MarketButton';
const Simulator = () => {
  const { data: userData } = useSession({ required: true });

  const [showResponse, setShowResponse] = useState<boolean>(false);
  const [responseData, setResponseData] = useState<any>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [formData, setFormData] = useState({
    days: 0,
    executeScheduled: false,
    executeDeclines: false,
    executePotentialRises: false,
    createCustomPlayer: true,
    player: {
      points: 0,
      age: { years: 0, months: 0, days: 0 },
      bloomYear: 0,
      isFallCliff: false,
      position: '',
      category: '',
      skills: {},
    },
    skills: [],
  });

  const handleButtonClick = async () => {
    // console.log(formData);
    setShowResponse(false); // Hide any previous response
    setIsLoading(true); // Set loading state
    setError(null); // Reset error state

    try {
      const response = await connectorAPI<any>(
        `/simulator/simulate-days`,
        'POST',
        formData,
        undefined,
        userData?.accessToken,
      );
      // console.log(response);
      setResponseData(response); // Update the response data
      setShowResponse(true); // Show the response
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Failed to fetch data from server.');
    } finally {
      setIsLoading(false); // Reset loading state
    }
  };

  const generateRandomPlayerAge = () => {
    const randomYears = Math.floor(Math.random() * (25 - 18 + 1)) + 18; // 18 to 25
    const randomMonths = Math.floor(Math.random() * 12); // 0 to 11
    const randomDays = Math.floor(Math.random() * 30); // 0 to 29
    const randomBloomYear = Math.floor(Math.random() * (21 - 18 + 1)) + 18; // 18 to 21

    setFormData((prev) => ({
      ...prev,
      player: {
        ...prev.player,
        age: {
          years: randomYears,
          months: randomMonths,
          days: randomDays,
        },
        bloomYear: randomBloomYear,
      },
    }));
  };

  const handleAgeChange = (field, value) => {
    setFormData((prevState) => ({
      ...prevState,
      player: {
        ...prevState.player,
        age: {
          ...prevState.player.age,
          [field]: value,
        },
      },
    }));
  };

  const handleInputChange = (field, value) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleNestedInputChange = (field, nestedField, value) => {
    setFormData((prev) => ({
      ...prev,
      [field]: { ...prev[field], [nestedField]: value },
    }));
  };

  const randomizePositionAndCategory = () => {
    const randomPosition =
      Object.values(PlayerPosition)[
        Math.floor(Math.random() * Object.values(PlayerPosition).length)
      ];
    const randomCategory =
      Object.values(PlayerCategory)[
        Math.floor(Math.random() * Object.values(PlayerCategory).length)
      ];

    setFormData((prevState) => ({
      ...prevState,
      player: {
        ...prevState.player,
        position: randomPosition, // Set random position
        category: randomCategory, // Set random category
      },
    }));
  };

  const randomizeSkills = () => {
    // Clear all existing skills in formData before randomizing
    const updatedSkills = {};

    const skillsList = Object.keys(PlayerSkill); // Get all available skill names
    const selectedSkills = []; // To store the randomly selected skills

    // Randomly shuffle the skills list and pick the first 8 skills
    while (selectedSkills.length < 8) {
      const randomSkill =
        skillsList[Math.floor(Math.random() * skillsList.length)];
      if (!selectedSkills.includes(randomSkill)) {
        selectedSkills.push(randomSkill);
      }
    }

    // Assign random values to the selected skills
    selectedSkills.forEach((skill) => {
      updatedSkills[skill] = {
        actual: Math.floor(Math.random() * (40 - 20 + 1)) + 20, // Random value between 20 and 40
        potential: Math.floor(Math.random() * (75 - 60 + 1)) + 60, // Random value between 60 and 75
      };
    });

    // Update the form data with only the selected 8 skills (clear the previous ones)
    setFormData((prevState) => ({
      ...prevState,
      player: {
        ...prevState.player,
        skills: updatedSkills, // Only 8 skills
      },
      skills: selectedSkills,
    }));
  };

  const handleSkillChange = (skill, field, value) => {
    setFormData((prev) => {
      const updatedSkills = {
        ...prev.player.skills,
        [skill]: { ...prev.player.skills[skill], [field]: value },
      };

      if (
        updatedSkills[skill].actual === '' &&
        updatedSkills[skill].potential === ''
      ) {
        const { [skill]: removed, ...remainingSkills } = updatedSkills; // Remove the skill
        return {
          ...prev,
          player: {
            ...prev.player,
            skills: remainingSkills,
          },
        };
      }

      return {
        ...prev,
        player: {
          ...prev.player,
          skills: updatedSkills,
        },
      };
    });
  };

  return (
    <Box sx={{ padding: 2 }}>
      {/* General Simulation Settings */}
      <Typography sx={{ textAlign: 'center' }} variant="h4">
        Player Lifecycle Simulation Settings
      </Typography>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          width: '100%',
        }}>
        <TextField
          label="Days"
          type="number"
          value={formData.days}
          onChange={(e) => handleInputChange('days', e.target.value)}
        />
        <FormControlLabel
          control={
            <Switch
              checked={formData.executeScheduled}
              onChange={(e) =>
                handleInputChange('executeScheduled', e.target.checked)
              }
            />
          }
          label="Execute Scheduled"
        />
        <FormControlLabel
          control={
            <Switch
              checked={formData.executeDeclines}
              onChange={(e) =>
                handleInputChange('executeDeclines', e.target.checked)
              }
            />
          }
          label="Execute Declines"
        />
        <FormControlLabel
          control={
            <Switch
              checked={formData.executePotentialRises}
              onChange={(e) =>
                handleInputChange('executePotentialRises', e.target.checked)
              }
            />
          }
          label="Execute Potential Rises"
        />
      </Box>

      {/* Custom Player Options */}
      <Typography sx={{ textAlign: 'center', padding: '10px' }} variant="h4">
        Player Settings
      </Typography>
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'start' }}>
        <MarketButton sx={{ marginRight: '10px' }} onClick={randomizeSkills}>
          Randomize Skills
        </MarketButton>
        <MarketButton
          sx={{ marginRight: '10px' }}
          onClick={generateRandomPlayerAge}>
          Randomize Age
        </MarketButton>
        <MarketButton
          sx={{ marginRight: '10px' }}
          onClick={randomizePositionAndCategory}>
          Randomize Category and Position
        </MarketButton>
      </Box>

      {/* <FormControlLabel
        control={
          <Switch
            checked={formData.createCustomPlayer}
            onChange={(e) =>
              handleInputChange('createCustomPlayer', e.target.checked)
            }
          />
        }
        label="Create Custom Player"
      /> */}
      <Box sx={{ marginTop: 2 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={3}>
            <TextField
              label="Year"
              type="number"
              fullWidth
              value={formData.player.age.years}
              onChange={(e) =>
                handleAgeChange('years', parseInt(e.target.value, 10))
              }
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              label="Month"
              type="number"
              fullWidth
              value={formData.player.age.months}
              onChange={(e) =>
                handleAgeChange('months', parseInt(e.target.value, 10))
              }
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              label="Days"
              type="number"
              fullWidth
              value={formData.player.age.days}
              onChange={(e) =>
                handleAgeChange('days', parseInt(e.target.value, 10))
              }
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              label="Bloom Year"
              type="number"
              fullWidth
              value={formData.player.bloomYear}
              onChange={(e) =>
                handleNestedInputChange('player', 'bloomYear', e.target.value)
              }
            />
          </Grid>
          {/* <Grid item xs={12} sm={4}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.player.isFallCliff}
                    onChange={(e) =>
                      handleNestedInputChange(
                        'player',
                        'isFallCliff',
                        e.target.checked,
                      )
                    }
                  />
                }
                label="Is Fall Cliff?"
              />
            </Grid> */}

          <Grid item xs={12} sm={6}>
            <Autocomplete
              options={Object.values(PlayerPosition)}
              value={formData.player.position}
              onChange={(e, value) =>
                handleNestedInputChange('player', 'position', value)
              }
              renderInput={(params) => (
                <TextField {...params} label="Position" />
              )}
              sx={{ marginTop: 2 }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <Autocomplete
              options={Object.values(PlayerCategory)}
              value={formData.player.category}
              onChange={(e, value) =>
                handleNestedInputChange('player', 'category', value)
              }
              renderInput={(params) => (
                <TextField {...params} label="Category" />
              )}
              sx={{ marginTop: 2 }}
            />
          </Grid>
        </Grid>

        {/* Skills */}
        <Typography
          variant="h6"
          gutterBottom
          sx={{ display: 'block', paddingTop: '10px', textAlign: 'center' }}>
          !!!! When choosing the skills please always specify the actual and
          potential value. !!!!
        </Typography>
        <Grid container sx={{ background: 'transparent' }}>
          {Object.keys(PlayerSkill).map((skill) => (
            <Grid
              item
              key={skill}
              sx={{
                padding: '10px',
                width: '12.5%', // Each item takes 12.5% of the row width
              }}>
              <Box>
                <Typography
                  sx={{
                    fontSize: '0.85rem',
                    textAlign: 'center',
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                  }}>
                  {skill}
                </Typography>
                <TextField
                  label="Actual"
                  type="number"
                  fullWidth
                  value={formData.player.skills[skill]?.actual || ''}
                  onChange={(e) =>
                    handleSkillChange(skill, 'actual', e.target.value)
                  }
                  sx={{ marginBottom: 1 }}
                />
                <TextField
                  label="Potential"
                  type="number"
                  fullWidth
                  value={formData.player.skills[skill]?.potential || ''}
                  onChange={(e) =>
                    handleSkillChange(skill, 'potential', e.target.value)
                  }
                />
              </Box>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Selected Skills */}
      <Autocomplete
        multiple
        options={Object.keys(formData.player.skills)} // Filter options based on player's existing skills
        value={formData.skills}
        onChange={(e, value) => handleInputChange('skills', value)}
        renderInput={(params) => (
          <TextField {...params} label="Skills to Train" />
        )}
        sx={{ marginTop: 2 }}
      />

      {/* Submit Button */}
      <Box sx={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
        <MarketButton sx={{ marginTop: 2 }} onClick={() => handleButtonClick()}>
          Submit
        </MarketButton>
      </Box>

      {isLoading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {showResponse && responseData && (
        <pre
          style={{
            background: '#f4f4f4',
            padding: '10px',
            borderRadius: '5px',
          }}>
          {JSON.stringify(responseData, null, 2)}
        </pre>
      )}
    </Box>
  );
};

export default Simulator;
