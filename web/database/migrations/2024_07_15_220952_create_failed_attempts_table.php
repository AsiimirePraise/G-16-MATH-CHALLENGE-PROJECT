<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('failed_attempts', function (Blueprint $table) {
            $table->id();
            $table->string('questions');
            $table->unsignedBigInteger('participant_challenge')->nullable();
            $table->unsignedBigInteger('participant')->nullable();
            $table->foreign('participant_challenge')->references('id')->on('participant_challenges')->onDelete('cascade');
            $table->foreign('participant')->references('id')->on('participants')->onDelete('cascade');
            $table->string('answers');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('failed_attempts');
    }
};
