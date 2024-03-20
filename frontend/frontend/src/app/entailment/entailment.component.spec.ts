import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntailmentComponent } from './entailment.component';

describe('EntailmentComponent', () => {
  let component: EntailmentComponent;
  let fixture: ComponentFixture<EntailmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EntailmentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EntailmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
