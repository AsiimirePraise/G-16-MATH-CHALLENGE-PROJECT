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
        Schema::create('challenge_question_answers', function (Blueprint $table) {
            $table->id();
            $table->unsignedBigInteger('challenge');
            $table->unsignedBigInteger('question');

            $table->foreign('challenge')->references('id')->on('challenges')->onDelete('cascade');
            $table->foreign('question')->references('id')->on('question_answers')->onDelete('cascade');

            $table->timestamps();

            $table->index('challenge');
            $table->index('question');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('challenge_question_answers');
    }
};
