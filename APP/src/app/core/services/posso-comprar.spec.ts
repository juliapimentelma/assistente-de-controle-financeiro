import { TestBed } from '@angular/core/testing';

import { PossoComprar } from './posso-comprar';

describe('PossoComprar', () => {
  let service: PossoComprar;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PossoComprar);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
