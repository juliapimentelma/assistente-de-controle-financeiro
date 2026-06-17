import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameProgress } from './game-progress';

describe('GameProgress', () => {
  let component: GameProgress;
  let fixture: ComponentFixture<GameProgress>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameProgress],
    }).compileComponents();

    fixture = TestBed.createComponent(GameProgress);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
