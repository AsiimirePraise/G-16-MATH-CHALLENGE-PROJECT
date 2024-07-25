<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class RenameEmailToRepresentativeEmailInSchoolsTable extends Migration
{
    public function up()
    {
        Schema::table('schools', function (Blueprint $table) {
            $table->renameColumn('email', 'representative_email');
        });
    }

    public function down()
    {
        Schema::table('schools', function (Blueprint $table) {
            $table->renameColumn('representative_email', 'email');
        });
    }
}
