import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PossoComprar } from './posso-comprar';

describe('PossoComprar', () => {
  let component: PossoComprar;
  let fixture: ComponentFixture<PossoComprar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PossoComprar],
    }).compileComponents();

    fixture = TestBed.createComponent(PossoComprar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
