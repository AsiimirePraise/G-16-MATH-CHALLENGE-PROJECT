<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class ParticipantChallenge extends Model
{
    use HasFactory;

    public function participant()
    {
        return $this->belongsTo(Participant::class, 'participant', 'id');
    }

    public function challenge()
    {
        return $this->belongsTo(Challenge::class, 'challenge', 'id');
    }
}
