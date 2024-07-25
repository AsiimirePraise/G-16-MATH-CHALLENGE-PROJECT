<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\School;

class SchoolController extends Controller
{
    public function create()
    {
        return view('schools.create');
    }

    public function store(Request $request)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'district' => 'required|string|max:255',
            'registration_number' => 'required|string|max:255|unique:schools',
            'representative_email' => 'required|email',
            'representative_name' => 'required|string|max:255',
        ]);

        $school = new School;

        $school->name = $request->input('name');
        $school->district = $request->input('district');
        $school->registration_number = $request->input('registration_number');
        $school->representative_email = $request->input('representative_email');
        $school->representative_name = $request->input('representative_name');

        $school->save();

        return redirect()->route('schools.index')->with('success', 'School registered successfully.');
    }


    public function edit(School $school) 
    {
        return view('schools.edit', compact('school'));
    }

    public function update(Request $request, School $school)
    {
        $request->validate([
            'name' => 'required|string|max:255',
            'district' => 'required|string|max:255',
            'registration_number' => 'required|string|max:255',
            'representative_email' => 'required|email',
            'representative_name' => 'required|string|max:255',
        ]);

        $school->name = $request->input('name');
        $school->district = $request->input('district');
        $school->registration_number = $request->input('registration_number');
        $school->representative_email = $request->input('representative_email');
        $school->representative_name = $request->input('representative_name');

        $school->save();

        return redirect()->route('schools.index')->with('success', 'school updated successfully.');
    }

    public function index()
    {
        $schools = School::all();

        return view('schools.index', compact('schools'));
    }

    public function delete(School $school)
    {
        $school->forceDelete();

        return redirect()->route('schools.index')->with('success', 'school deleted succesfully');
    }

}

