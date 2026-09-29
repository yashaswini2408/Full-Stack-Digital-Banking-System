import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Login } from './components/login/login';
import { Registration } from './components/registration/registration';
import { AccountDetails } from './components/account-details/account-details';
import { AdminDashboard } from './components/admin-dashboard/admin-dashboard';
import { ApplyLoan } from './components/apply-loan/apply-loan';
import { CustomerDashboard } from './components/customer-dashboard/customer-dashboard';
import { CustomerList } from './components/customer-list/customer-list';
import { CustomerRegister } from './components/customer-register/customer-register';
import { CustomerUpdate } from './components/customer-update/customer-update';
import { CustomerViewProfile } from './components/customer-view-profile/customer-view-profile';
import { EmployeeDashboard } from './components/employee-dashboard/employee-dashboard';
import { LoanReview } from './components/loan-review/loan-review';
import { ManageUsers } from './components/manage-users/manage-users';
import { MyAccounts } from './components/my-accounts/my-accounts';
import { MyLoans } from './components/my-loans/my-loans';
import { OpenAccount } from './components/open-account/open-account';
import { TransactionView } from './components/transaction-view/transaction-view';
import { Transactions } from './components/transactions/transactions';
import { ViewAccounts } from './components/view-accounts/view-accounts';
import { ViewLoans } from './components/view-loans/view-loans';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { authInterceptor } from './interceptors/auth-interceptor';
import { ManageBankEmployees } from './components/manage-bank-employees/manage-bank-employees';
import { Home } from './components/home/home';

@NgModule({
  declarations: [
    App,
    Login,
    Registration,
    AccountDetails,
    AdminDashboard,
    ApplyLoan,
    CustomerDashboard,
    CustomerList,
    CustomerRegister,
    CustomerUpdate,
    CustomerViewProfile,
    EmployeeDashboard,
    LoanReview,
    ManageUsers,
    MyAccounts,
    MyLoans,
    OpenAccount,
    TransactionView,
    Transactions,
    ViewAccounts,
    ViewLoans,
    ManageBankEmployees,
    Home
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    CommonModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ],
  bootstrap: [App]
})
export class AppModule { }
