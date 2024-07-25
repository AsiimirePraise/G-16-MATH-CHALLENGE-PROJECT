<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Carbon\Carbon;
use App\Models\School;
use App\Models\Challenge;
use App\Models\Participant;
use App\Models\ChallengeQuestionAnswer;
use App\Models\ParticipantChallenge;
use App\Models\QuestionAnswer;
use App\Models\FailedAttempt;
use App\Models\Attempt;


class DashboardController extends Controller
{
    public function index()
    {


        $schools = School::all();
        $challenges = Challenge::all();
        $participants = Participant::all();
        $challenge_questions = ChallengeQuestionAnswer::all();
        $attempts = ParticipantChallenge::all();
        $questions = QuestionAnswer::all();

        // Most correctly answered questions
        $most_correctly_answered_questions = QuestionAnswer::select('question_answers.*')
        ->selectRaw('COUNT(*) as count')
        ->join('attempts', 'question_answers.id', '=', 'attempts.question')
        ->where('attempts.status', 'correct')
        ->groupBy('question_answers.id')
        ->orderByRaw('COUNT(*) DESC')
            ->limit(5)
            ->get();


        // School rankings
        $school_rankings = School::select('schools.*')
        ->selectRaw('COUNT(DISTINCT participant_challenges.id) as challenge_count')
        ->selectRaw('AVG((participant_challenges.score / participant_challenges.total) * 100) as average_score')
        ->join('participants', 'schools.registration_number', '=', 'participants.registration_number')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->groupBy('schools.id')
        ->orderBy('average_score', 'desc')
        ->get();


        //  Performance of schools and participants over the years and time
        $schools_over_time = School::select(
            'schools.id AS school_id',
            'schools.name AS school_name',
            DB::raw('MONTH(participant_challenges.created_at) AS month'),
            DB::raw('AVG((participant_challenges.score / participant_challenges.total) * 100) AS avg_score')
        )
        ->join('participants', 'schools.registration_number', '=', 'participants.registration_number')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->whereRaw('participant_challenges.created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH)')
        ->groupBy('schools.id', 'schools.name', DB::raw('MONTH(participant_challenges.created_at)'))
        ->orderBy('schools.id')
        ->orderBy(DB::raw('MONTH(participant_challenges.created_at)'))
        ->get();

        $school_performance_over_time = [];
        foreach ($schools_over_time as $record) {
            $school_id = $record->school_id;
            $month = $record->month;

            if (!isset($school_performance_over_time[$school_id])) {
                $school_performance_over_time[$school_id] = [
                    'name' => $record->school_name,
                    'scores' => array_fill(0, 12, 0)
                ];
            }

            $school_performance_over_time[$school_id]['scores'][$month - 1] = round($record->avg_score, 2);
        }

        foreach ($school_performance_over_time as &$school) {
            $school['scores'] = array_values($school['scores']);
        }

        // Percentage repetition of questions for a given participant across attempts
        $random_participant = Participant::inRandomOrder()->first();

        $attempted_once = Attempt::select('question', DB::raw('COUNT(question) as counts'))
        ->where('participant', $random_participant->id)
        ->groupBy('question')
        ->having('counts', '=', 1)
        ->get();


        $attempted_all = Attempt::All();

        $participant_percentage_repetition["participant"] = $random_participant;
        $participant_percentage_repetition["repetition"] = round(($attempted_once->count() / $attempted_all->count()) * 100, 2);


        
        
        $random_challenge = Challenge::inRandomOrder()->first();

        $challenge_school = School::select('schools.*', DB::raw('AVG((participant_challenges.score / participant_challenges.total) * 100) as counts'))
        ->join('participants', 'participants.registration_number', '=', 'schools.registration_number')
        ->join('participant_challenges', 'participant_challenges.participant', '=', 'participants.id')
        ->join('challenges', 'participant_challenges.challenge', '=', 'challenges.id')
        ->where('participant_challenges.challenge', $random_challenge->id)
        ->groupBy('schools.id')
        ->orderBy('counts')
        ->get();

        $challenges_worst_schools["challenge"] = $random_challenge;
        $challenges_worst_schools["schools"] = $challenge_school;


        


        // Todo: List of participants with incomplete challenges
        $participants_with_incomplete_challenges = Participant::join('failed_attempts', 'failed_attempts.participant', '=', 'participants.id')
        ->select('*')
        ->limit(5)
        ->get();






        $startOfWeek = Carbon::now()->startOfWeek();
        $endOfWeek = Carbon::now()->endOfWeek();

        $now = Carbon::now();
        $oneWeekAgo = $now->subDays(7);
        
        $weeklyParticipants = Participant::select(
            DB::raw('DAYOFWEEK(created_at) as dayOfWeek'),
            DB::raw('COUNT(*) as count')
        )
            ->where('created_at', '>=', $oneWeekAgo)
            ->groupBy(DB::raw('DAYOFWEEK(created_at)'))
            ->get();
        $weekly_participants = array_fill(0, 7, 0);
        foreach ($weeklyParticipants as $participant) {
            $index = $participant->dayOfWeek - 1;
            $weekly_participants[$index] = $participant->count;
        }

        $attemptsByDay = ParticipantChallenge::whereBetween('created_at', [$startOfWeek, $endOfWeek])
            ->selectRaw('DAYOFWEEK(created_at) as day, COUNT(*) as count')
            ->groupBy('day')
            ->pluck('count', 'day');

        $weekly_attempts = array_fill(0, 7, 0);

        foreach ($attemptsByDay as $day => $count) {
            $index = $day - 1;
            $weekly_attempts[$index] = $count;
        }
        $best_participant = Participant::select('participants.*')
        ->selectRaw('AVG((participant_challenges.score / participant_challenges.total) * 100) as average_score')
        ->selectRaw('schools.name as school_name')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->join('schools', 'participants.registration_number', '=', 'schools.registration_number')
        ->groupBy('participants.id')
        ->orderBy('average_score', 'desc')
        ->first();


        // best participants in all closed challenges
        $all_closed_challenges = Challenge::where('closing_date', '<', now())
        ->select('*')
        ->get();

        $best_participants_in_all_challenges = Participant::select(
            'participants.registration_number AS registration_number',
            'participants.firstname',
            'participants.lastname',
            'participants.id AS participant_id',
            'participants.username',
            'challenges.id AS challenge_id',
            'participants.image_path',
            DB::raw('AVG((participant_challenges.score / participant_challenges.total) * 100) AS average_score')
        )
        ->join('participant_challenges', 'participant_challenges.participant', '=', 'participants.id')
        ->join('challenges', 'participant_challenges.challenge', '=', 'challenges.id')
        ->groupBy('participants.id', 'challenges.id')
        ->orderByRaw('average_score DESC')
        ->get();


        $challenge_two_best_performers = $all_closed_challenges->map(function ($challenge) use ($best_participants_in_all_challenges) {
            $top_two_participants = $best_participants_in_all_challenges->where("challenge_id", '=', $challenge->id)->take(2)->map(function ($participant) {
                return [
                    'id' => $participant->participant_id,
                    'username' => $participant->username,
                    'firstname' => $participant->firstname,
                    'lastname' => $participant->lastname,
                    'image_path' => $participant->image_path,
                    'registration_number' => $participant->registration_number,
                    'score' => round($participant->average_score, 1)
                ];
            });

            return [
                    'id' => $challenge->id,
                    'name' => $challenge->challenge_name,
                    'difficulty' => $challenge->difficulty,
                    'opening_date' => $challenge->start_date,
                    'closing_date' => $challenge->closing_date,
                    'top_two_participants' => $top_two_participants->toArray()
                ];
        })->values()->toArray();



        $colors = [
            'rgba(255, 99, 132, 0.8)', // Pink
            'rgba(255, 159, 64, 0.8)', // Orange
            'rgba(255, 205, 86, 0.8)', // Yellow
            'rgba(75, 192, 192, 0.8)', // Teal
            'rgba(153, 102, 255, 0.8)', // Purple
            'rgba(201, 203, 207, 0.8)', // Grey
            'rgba(255, 99, 71, 0.8)', // Tomato
            'rgba(50, 205, 50, 0.8)', // Lime Green
            'rgba(255, 215, 0, 0.8)', // Gold
            'rgba(255, 0, 255, 0.8)', // Magenta
            'rgba(139, 69, 19, 0.8)', // Saddle Brown
            'rgba(220, 20, 60, 0.8)', // Crimson
        ];

        return view('dashboard.index', compact(
            'most_correctly_answered_questions', 
            'school_rankings',
            'school_performance_over_time',
            'participant_percentage_repetition',
            'challenges_worst_schools',
            'participants_with_incomplete_challenges',

            'colors',

            'challenge_two_best_performers', 
            'schools', 
            'challenges', 
            'participants', 
            'challenge_questions', 
            'attempts', 
            'questions',
            'weekly_participants', 
            'weekly_attempts', 
            'best_participant',
        ));
    }
}
