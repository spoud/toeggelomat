import {describe, expect, it} from 'vitest';
import {Score} from './types';

describe('Score', () => {
  it('is invalid until both scores are selected', () => {
    expect(new Score().isInValid()).toBe(true);
    expect(new Score(7, 3).isInValid()).toBe(false);
  });

  it('auto-selects 7 for the other team when a non-winning red score is picked first', () => {
    const score = new Score().redScoreSelected(3);
    expect(score.blueScore).toBe(7);
    expect(score.redScore).toBe(3);
  });

  it('auto-selects 7 for the other team when a non-winning blue score is picked first', () => {
    const score = new Score().blueScoreSelected(4);
    expect(score.blueScore).toBe(4);
    expect(score.redScore).toBe(7);
  });

  it('does not override an already-selected blue score when red is picked', () => {
    const score = new Score(5, -1).redScoreSelected(2);
    expect(score.blueScore).toBe(5);
    expect(score.redScore).toBe(2);
  });
});
