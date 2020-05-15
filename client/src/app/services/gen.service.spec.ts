import { TestBed } from '@angular/core/testing';

import { GenService } from './gen.service';

describe('GenService', () => {
  let service: GenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
