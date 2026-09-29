import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerRegister } from './components/customer-register/customer-register';
import { CustomerViewProfile } from './components/customer-view-profile/customer-view-profile';
import { CustomerUpdate } from './components/customer-update/customer-update';
import { OpenAccount } from './components/open-account/open-account';
import { MyAccounts } from './components/my-accounts/my-accounts';
import { AccountDetails } from './components/account-details/account-details';
import { Transactions } from './components/transactions/transactions';
import { ViewLoans } from './components/view-loans/view-loans';
import { ApplyLoan } from './components/apply-loan/apply-loan';
import { MyLoans } from './components/my-loans/my-loans';
import { Registration } from './components/registration/registration';
import { Login } from './components/login/login';
import { CustomerDashboard } from './components/customer-dashboard/customer-dashboard';
import { EmployeeDashboard } from './components/employee-dashboard/employee-dashboard';
import { CustomerList } from './components/customer-list/customer-list';
import { TransactionView } from './components/transaction-view/transaction-view';
import { ViewAccounts } from './components/view-accounts/view-accounts';
import { LoanReview } from './components/loan-review/loan-review';
import { AdminDashboard } from './components/admin-dashboard/admin-dashboard';
import { ManageUsers } from './components/manage-users/manage-users';
import { authGuard } from './auth-guard';
import { ManageBankEmployees } from './components/manage-bank-employees/manage-bank-employees';
import { Home } from './components/home/home';

const routes: Routes = [ { path: '', component: Home },
  { path: 'register', component: Registration },
  { path: 'login', component: Login },
  {
    path: 'customer-dashboard', 
    component: CustomerDashboard,
    canActivate: [authGuard],
    children: [
      { path: 'customer-register', component: CustomerRegister },
      { path: 'customer-view-profile/:userId', component: CustomerViewProfile },
      { path: 'customer-update/:customerId', component: CustomerUpdate },
      { path: 'open-account', component: OpenAccount },
      { path: 'my-accounts/:customerId', component: MyAccounts },
      { path: 'account-details', component: AccountDetails },
      { path: 'account-details/:accountId',component: AccountDetails },
      { path: 'transactions', component: Transactions },
      { path: 'view-loans', component: ViewLoans },
      { path: 'apply-loan', component: ApplyLoan },
      { path: 'my-loans/:customerId', component: MyLoans }
    ] 
  },
  {
    path: 'employee-dashboard',
    component: EmployeeDashboard,
    canActivate: [authGuard],
    children: [
      { path: 'view-customers', component: CustomerList },
      { path: 'view-transactions', component: TransactionView },
      { path: 'view-accounts', component: ViewAccounts },
      { path: 'loan-review', component: LoanReview }

    ]
  },
  {
    path: 'admin-dashboard',
    component: AdminDashboard,
    canActivate: [authGuard],
    children: [
      { path: 'manage-users', component: ManageUsers },
      { path: 'manage-bank-employees', component: ManageBankEmployees}
    ]
  },
  { path: '**', redirectTo: 'login' }
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
