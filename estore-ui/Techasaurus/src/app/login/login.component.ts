import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AccountService } from '../account.service';
import { Account } from '../account';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginForm!: FormGroup;

    constructor(private formBuilder: FormBuilder, private router: Router, private accountService: AccountService) { }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    login(): void {
        const username = this.loginForm.get('username')?.value;
        const password = this.loginForm.get('password')?.value;

        this.accountService.loginAccount(username, password).subscribe({
            next: (account: Account) => {
                if (account && account.isLoggedIn) {
                    this.loginForm.reset();
                    this.router.navigate(['/dashboard']).then(() => {
                        window.location.reload();
                    });
                } else {
                    alert('Invalid username or password. Please try again.');
                }
            },
            error: (err: any) => {
                alert('Error logging in. Please Try again.')
                console.log(err);
            }
        });
    }
}
