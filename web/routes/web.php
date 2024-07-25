<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

use App\Http\Controllers\DashboardController;
use App\Http\Controllers\ProfileController;
use App\Http\Controllers\RegisterController;
use App\Http\Controllers\SessionsController;
use App\Http\Controllers\SchoolController;
use App\Http\Controllers\ChallengeController;

// school routes
Route::get('/schools/create', [SchoolController::class, 'create'])->name('schools.create');
Route::post('/schools/create', [SchoolController::class, 'store'])->name('schools.store');

Route::get('/schools/{school}/edit', [SchoolController::class, 'edit'])->name('schools.edit');
Route::post('/schools/{school}/edit', [SchoolController::class, 'update'])->name('schools.update');

Route::get('/schools', [SchoolController::class, 'index'])->name('schools.index');
Route::get('/schools/{school}/delete', [SchoolController::class, 'delete'])->name('schools.delete');

// challenge routes
Route::get('/challenges', [ChallengeController::class, 'index'])->name('challenges');
Route::get('challenges/create', [ChallengeController::class, 'create'])->name('create-challenge');
Route::post('challenges/create', [ChallengeController::class, 'store']);

Route::get('challenges/{challenge}/config', [ChallengeController::class, 'config'])->name('config-challenge');
Route::post('challenges/{challenge}/config', [ChallengeController::class, 'update']);

Route::get('challenges/{challenge}/delete', [ChallengeController::class, 'delete'])->name('delete-challenge');

Route::get('challenges/{challenge}/upload-question-answers', [ChallengeController::class, 'upload'])->name('upload-questions-answers');
Route::post('challenges/{challenge}/upload-question-answers', [ChallengeController::class, 'add']);
            

Route::get('/', function () {return redirect('sign-in');})->middleware('guest');
Route::get('/dashboard', [DashboardController::class, 'index'])->middleware('auth')->name('dashboard');
Route::get('sign-up', [RegisterController::class, 'create'])->middleware('guest')->name('register');
Route::post('sign-up', [RegisterController::class, 'store'])->middleware('guest');
Route::get('sign-in', [SessionsController::class, 'create'])->middleware('guest')->name('login');
Route::post('sign-in', [SessionsController::class, 'store'])->middleware('guest');
Route::post('verify', [SessionsController::class, 'show'])->middleware('guest');
Route::post('reset-password', [SessionsController::class, 'update'])->middleware('guest')->name('password.update');
Route::get('verify', function () {
	return view('sessions.password.verify');
})->middleware('guest')->name('verify'); 
Route::get('/reset-password/{token}', function ($token) {
	return view('sessions.password.reset', ['token' => $token]);
})->middleware('guest')->name('password.reset');

Route::post('sign-out', [SessionsController::class, 'destroy'])->middleware('auth')->name('logout');
Route::get('profile', [ProfileController::class, 'create'])->middleware('auth')->name('profile');
Route::post('user-profile', [ProfileController::class, 'update'])->middleware('auth');
Route::group(['middleware' => 'auth'], function () {
	Route::get('billing', function () {
		return view('pages.billing');
	})->name('billing');
	Route::get('tables', function () {
		return view('pages.tables');
	})->name('tables');
	Route::get('rtl', function () {
		return view('pages.rtl');
	})->name('rtl');
	Route::get('virtual-reality', function () {
		return view('pages.virtual-reality');
	})->name('virtual-reality');
	Route::get('notifications', function () {
		return view('pages.notifications');
	})->name('notifications');
	Route::get('static-sign-in', function () {
		return view('pages.static-sign-in');
	})->name('static-sign-in');
	Route::get('static-sign-up', function () {
		return view('pages.static-sign-up');
	})->name('static-sign-up');
	Route::get('user-management', function () {
		return view('pages.laravel-examples.user-management');
	})->name('user-management');
	Route::get('user-profile', function () {
		return view('pages.laravel-examples.user-profile');
	})->name('user-profile');
});