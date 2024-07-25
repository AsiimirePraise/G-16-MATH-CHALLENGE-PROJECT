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
        Schema::table('attempts', function (Blueprint $table) {
            $table->unsignedBigInteger('question')->nullable(); // Add question_id column
            $table->unsignedBigInteger('participant')->nullable(); // Add question_id column
            $table->foreign('question')->references('id')->on('question_answers')->onDelete('cascade');
            $table->foreign('participant')->references('id')->on('participants')->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('attempts', function (Blueprint $table) {
            $table->dropColumn(['question', 'participant']);
        });
    }
};
