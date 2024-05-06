import { assign, setup } from 'xstate'

type MatchContext = {
  currentTick: number
  totalTicks: number
}

export const matchMachine = setup({
  types: {
    context: {} as MatchContext,
  },
  actions: {
    initializeMatchData: assign({ currentTick: 0, totalTicks: 90 }),
    nextTick: assign(({ context }) => ({ currentTick: context.currentTick + 1 })),
  },
  guards: {
    isHalfTime: ({ context }) => context.currentTick === context.totalTicks / 2,
    isFullTime: ({ context }) => context.currentTick >= context.totalTicks,
  },
}).createMachine({
  /** @xstate-layout N4IgpgJg5mDOIC5QFsCGAXAxgCwLSzACcA3AS0zFzR1IDswA6NLbAZXVUPQGJ6APdAG0ADAF1EoAA4B7WKXSlptCSD6IAjAHZhDAKwAOTQBYAnNqMA2TZovrdAGhABPRACZ1+hvpPqL+uxbCwurC+roAvuGOzDj4RGQUVKg09AyEcGDoAAoANqhOvGACIuJIIDJyCkoqaghaOgbGZsKW1rYOzhp+DEZNNkauhsIAzLpGkdEYsQQk5JTU2HSMknkFsJjS6SUqFfKKymW19XqGpuZWNnaOLnUeDNamQyahFhbDrhMgMXgzCfPJi1SK3yhWKYh2sj21UOiEsnkGml06hMBleJlcHRuHnUXlMJn0w0JoTGlk+3zis0SCyWDGBBX4QnUpSkkKqB1AtThDARSJR+jRGOuiH0rh6uiCLQsuiRmhM+LJUx+8TmSRSy1WoKErmZ5VZ+xqbjeDFeGOR3mErlCviFtwsDGGTSsKLGBPUCpYFL+qsBjGwqByADMACqkZBgTXbMq7NkGhAGHFGB26TTufQDXQWIw29TvBjqPEmYZy4bBFPDSJREC0aQQOAqcm-FXU+gQyr6mEIXAWG1dhgS-sDoKad3TZVUgE077sTjoVtQ9mqWGuG2phhy3yWExWALDysNsf-NVpDLZVZzmMd8XciX50aaNNadTZwI9Hy2QsGaXDN17xWepsTkCZ5Rnq0IcogJaeFo4rCM8mh2OodiaNm8F9k0yauIW7wjB8v4eo245Hn6gYhmG57tuBCBWMM9oSry4qBLoy6dHUNj2q8FiuJxgTaEW+gjkqlKHj6DABgArjkORBgAkmRIFtmBi5UZoNElkE9HCIxzE3P43JynKRi6A6yK2LukRAA */
  id: 'match-machine',
  initial: 'matchStart',
  context: {
    currentTick: 0,
    totalTicks: 90,
  },
  states: {
    matchStart: {
      on: {
        next: {
          target: 'play',
          actions: ['initializeMatchData'],
        },
      },
    },
    resetPlay: {
      on: {
        next: {
          target: 'play',
        },
      },
    },
    play: {
      on: {
        score: {
          target: 'resetPlay',
        },
        next: [
          {
            guard: 'isHalfTime',
            target: 'halfTime',
          },
          { guard: 'isFullTime', target: 'fullTime' },
          { target: 'play', reenter: true, actions: ['nextTick'] },
        ],
      },
    },
    halfTime: {
      on: {
        next: {
          target: 'play',
          actions: ['nextTick'],
        },
      },
    },
    fullTime: {
      type: 'final',
    },
  },
})
