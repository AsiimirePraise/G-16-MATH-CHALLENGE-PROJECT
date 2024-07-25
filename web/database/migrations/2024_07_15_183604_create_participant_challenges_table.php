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
        Schema::create('participant_challenges', function (Blueprint $table) {
            $table->id();
            $table->unsignedBigInteger('participant');
            $table->unsignedBigInteger('challenge');
            $table->integer('score');

            $table->foreign('participant')->references('id')->on('participants')->onDelete('cascade');
            $table->foreign('challenge')->references('id')->on('challenges')->onDelete('cascade');

            $table->index('participant');
            $table->index('challenge');
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
        Schema::dropIfExists('participant_challenges');
    }
};
