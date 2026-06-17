import { TestBed } from '@angular/core/testing';

import { Extrato } from './extrato';

describe('Extrato', () => {
  let service: Extrato;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Extrato);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
