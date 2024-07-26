<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

use App\Models\School;
use App\Models\Challenge;
use App\Models\Participant;
use App\Models\ParticipantChallenge;

class HomeController extends Controller
{
    // home page code
    public function home()
    {
        $schools = School::all();
        $challenges = Challenge::all();
        $participants = Participant::all();
        $attempts = ParticipantChallenge::all();

        // School rankings
        $school_rankings = School::select('schools.*')
        ->selectRaw('COUNT(DISTINCT participant_challenges.id) as challenge_count')
        ->selectRaw('AVG((participant_challenges.score / participant_challenges.total) * 100) as average_score')
        ->join('participants', 'schools.registration_number', '=', 'participants.registration_number')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->groupBy('schools.id')
        ->orderBy('average_score', 'desc')
        ->get();

        $best_participant = Participant::select('participants.*')
        ->selectRaw('AVG((participant_challenges.score / participant_challenges.total) * 100) as average_score')
        ->selectRaw('schools.name as school_name')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->join('schools', 'participants.registration_number', '=', 'schools.registration_number')
        ->groupBy('participants.id')
        ->orderBy('average_score', 'desc')
        ->first();


        $schools_over_time = School::select(
            'schools.id AS school_id',
            'schools.name AS school_name',
            DB::raw('YEAR(participant_challenges.created_at) AS year'),
            DB::raw('AVG((participant_challenges.score / participant_challenges.total) * 100) AS avg_score')
        )
        ->join('participants', 'schools.registration_number', '=', 'participants.registration_number')
        ->join('participant_challenges', 'participants.id', '=', 'participant_challenges.participant')
        ->whereRaw('participant_challenges.created_at >= DATE_SUB(NOW(), INTERVAL 10 YEAR)')
        ->groupBy(DB::raw('YEAR(participant_challenges.created_at)'), 'schools.id')
        ->orderBy(DB::raw('YEAR(participant_challenges.created_at)'))
        ->get();


        $school_performance_over_time = [];
        $currentYear = date('Y');
        $startYear = $currentYear - 9;
        
        foreach ($schools_over_time as $record) {
            $school_id = $record->school_id;
            $year = $record->year;

            if (!isset($school_performance_over_time[$school_id])) {
                $school_performance_over_time[$school_id] = [
                    'name' => $record->school_name,
                    'scores' => array_fill(0, 10, 0)
                ];
            }

            $index = $year - $startYear; // Calculate the index based on the start year
            if ($index >= 0 && $index < 10) {
                $school_performance_over_time[$school_id]['scores'][$index] = round($record->avg_score, 2);
            }
        }

        foreach ($school_performance_over_time as &$school) {
            $school['scores'] = array_values($school['scores']);
        }


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
            'schools.name AS school',
            DB::raw('AVG((participant_challenges.score / participant_challenges.total) * 100) AS average_score')
        )
        ->join('participant_challenges', 'participant_challenges.participant', '=', 'participants.id')
        ->join('challenges', 'participant_challenges.challenge', '=', 'challenges.id')
        ->join('schools', 'participants.registration_number', '=', 'schools.registration_number')
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
                    'school' => $participant->school,
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


        return view("home.index", compact("schools", "participants", "school_rankings", "challenges", "attempts", "best_participant",  "school_performance_over_time", "colors", "challenge_two_best_performers", "challenges_worst_schools"));
    }
}
