import { Component, OnInit, AfterViewInit } from '@angular/core';
import { particles } from '../assets/particleConfig/particleConfig.json';
import { GenService } from './services/gen.service.js';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Gen } from './models/gen.js';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  showIndex = true;
  myStyle: object = {};
  myParams: object = {};
  paramss: object = {};
  width = 100;
  height = 100;

  insertForm: FormGroup;
  
  genes: Array<Gen> = [];

  hayGenes: boolean;

  inserted: boolean;

  formatCorrect: boolean = true;

  constructor(private _genService: GenService){
    this.insertForm = new FormGroup({
      gen: new FormControl('', [Validators.required])
    });
  }

  GetStarted() {
    this.showIndex = false;
  }

  ngOnInit(): void {
    this.myStyle = {
      position: 'absolute',
      width: '100%',
      height: '100%',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0
    };

    
    this.paramss = JSON.parse(JSON.stringify(particles));
    this.myParams = {
      particles: this.paramss
    };
    console.log(this.myParams);

    ////GET GENES
    this._genService.getGenes().subscribe(
      res => {
        this.genes = res;

        if(this.genes.length <= 0){
          this.hayGenes=true;
        }else{
          this.hayGenes=false;
        }

        console.warn(this.genes);
      },
      err => console.log(err)
    );

  }

  insertGen(): void{

  
    console.log('LO INSERTE');
    console.log(this.insertForm.value['gen']);

    let gen = this.insertForm.value['gen'];

    if(/^[a-zA-Z0-9]{3,}$/.test(gen)){

      this.formatCorrect = true;

      this.inserted = true;
      this._genService.insertGen(gen).subscribe(
        res => {
          console.log(res);
          this.inserted = false;
          this.hayGenes=false;
          this.insertForm.reset();
          this.ngOnInit();
          
        },
        error => console.log(error)
      );

    }else{
      this.formatCorrect=false;
    }

    

  }

  truncate(): void{


    console.log('voy a eliminar');


    Swal.fire({
      title: 'Estas seguro?',
      text: "Esta acción no podrá revertirse.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Si, eliminar!'
    }).then((result) => {

      if (result.value) {

        this._genService.truncate().subscribe(
          res => {
            console.log(res);
            Swal.fire(
              'Eliminado!',
              'Los genes han sido eliminados correctamente',
              'success'
            );
            this.ngOnInit();
            this.formatCorrect=true;
          },
          err => console.log(err)
        );

        

      }

    });

  }

}
