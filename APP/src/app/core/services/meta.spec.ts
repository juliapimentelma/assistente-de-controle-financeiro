import { TestBed } from '@angular/core/testing';

import { Meta } from './meta';

describe('Meta', () => {
  let service: Meta;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Meta);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
