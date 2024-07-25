<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use PhpOffice\PhpSpreadsheet\IOFactory;
use App\Models\Challenge;
use App\Models\QuestionAnswer;
use App\Models\ChallengeQuestionAnswer;

class ChallengeController extends Controller
{
    public function index()
    {
        $challenges = Challenge::All();

        return view('challenges.index', compact('challenges'));
    }

    public function create()
    {
        return view('challenges.create');
    }

    public function store(Request $request)
    {
        $request->validate([
                'challenge_name' => 'required|string|max:255',
                'difficulty' => 'required|string|max:255',
                'time_allocation' => 'required|integer|min:1',
                'start_date' => 'required|date',
                'closing_date' => 'required|date',
            ]);

        $challenge = new Challenge;
        $challenge->challenge_name = $request->input('challenge_name');
        $challenge->difficulty = $request->input('difficulty');
        $challenge->time_allocation = $request->input('time_allocation');
        $challenge->start_date = $request->input('start_date');
        $challenge->closing_date = $request->input('closing_date');

        $challenge->save();

        return redirect()->route('challenges')->with('success', 'challenge created successfully');
    }

    public function config(Challenge $challenge)
    {
        return view('challenges.config', compact('challenge'));
    }

    public function update(Request $request, Challenge $challenge)
    {
        $request->validate([
            'challenge_name' => 'required|string|max:255',
            'difficulty' => 'required|string|max:255',
            'time_allocation' => 'required|integer|min:1',
            'start_date' => 'required|date',
            'closing_date' => 'required|date',
        ]);

        $challenge->challenge_name = $request->input('challenge_name');
        $challenge->difficulty = $request->input('difficulty');
        $challenge->time_allocation = $request->input('time_allocation');
        $challenge->start_date = $request->input('start_date');
        $challenge->closing_date = $request->input('closing_date');

        $challenge->save();

        return redirect()->route('challenges')->with('success', 'challenge updated successfully');
    }

    public function delete(Challenge $challenge)
    {
        $challenge->forceDelete();

        return redirect()->route('challenges')->with('success', 'challenge deleted succesfully');
    }

    public function upload(Challenge $challenge)
    {
        return view('challenges.upload', compact('challenge'));
    }

    public function add(Request $request, Challenge $challenge)
    {
        $request->validate([
            'questions' => 'required|file|mimes:xlsx,xls',
            'answers' => 'required|file|mimes:xlsx,xls',
        ]);

        $questions_file = $request->file('questions');
        $questions_spreadsheet = IOFactory::load($questions_file->getPathname());

        $answers_file = $request->file('answers');
        $answers_spreadsheet = IOFactory::load($answers_file->getPathname());

        $questions_sheet = $questions_spreadsheet->getActiveSheet();
        $answers_sheet = $answers_spreadsheet->getActiveSheet();

        // Get the highest row and column numbers referenced in the worksheet
        $highest_questions_row= $questions_sheet->getHighestRow();

        for ($row = 2; $row <= $highest_questions_row; $row++) { // Assuming first row is header
            $question = $questions_sheet->getCell('A' . $row)->getValue();
            $answer = $answers_sheet->getCell('A' . $row)->getValue();
            $score = $questions_sheet->getCell('B' . $row)->getValue();

            // Insert data into the database
            $questionAnswer = new QuestionAnswer;
            $questionAnswer->question = $question;
            $questionAnswer->answer = $answer;
            $questionAnswer->score = $score;
            $questionAnswer->save();

            $challenge_question_answer = new ChallengeQuestionAnswer;
            $challenge_question_answer->challenge = $challenge->id;
            $challenge_question_answer->question = $questionAnswer->id;
            $challenge_question_answer->save();
        }

        return redirect()->route('challenges')->with('success', 'question and answers successfully');
    }

}
