import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountermodelComponent } from './countermodel.component';

describe('CountermodelComponent', () => {
  let component: CountermodelComponent;
  let fixture: ComponentFixture<CountermodelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountermodelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CountermodelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
