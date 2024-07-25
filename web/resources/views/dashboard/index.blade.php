<x-layout bodyClass="g-sidenav-show  bg-gray-200">
    <x-navbars.sidebar activePage='dashboard'></x-navbars.sidebar>
    <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg mt-4">
        <!-- Navbar -->
        {{-- <x-navbars.navs.auth titlePage="Dashboard"></x-navbars.navs.auth> --}}
        <!-- End Navbar -->
        <div class="container-fluid py-4">
            <div class="row">
                <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
                    <div class="card">
                        <div class="card-header p-3 pt-2">
                            <div class="icon icon-lg icon-shape bg-gradient-dark shadow-dark text-center border-radius-xl mt-n4 position-absolute">
                                <i class="material-icons opacity-10">school</i>
                            </div>
                            <div class="text-end pt-1">
                                <p class="text-sm mb-0 text-capitalize">Schools</p>
                                <h4 class="mb-0">{{ $schools->count() }}</h4>
                            </div>
                        </div>
                        <hr class="dark horizontal my-0">
                        <div class="card-footer p-3">
                            <p class="mb-0">Registered schools</p>
                        </div>

                    </div>
                </div>
                <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
                    <div class="card">
                        <div class="card-header p-3 pt-2">
                            <div class="icon icon-lg icon-shape bg-gradient-primary shadow-primary text-center border-radius-xl mt-n4 position-absolute">
                                <i class="material-icons opacity-10">book</i>
                            </div>
                            <div class="text-end pt-1">
                                <p class="text-sm mb-0 text-capitalize">Challenges</p>
                                <h4 class="mb-0">{{ $challenges->count() }}</h4>
                            </div>
                        </div>
                        <hr class="dark horizontal my-0">
                        <div class="card-footer p-3">
                            <p class="mb-0">Added challenges</p>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
                    <div class="card">
                        <div class="card-header p-3 pt-2">
                            <div class="icon icon-lg icon-shape bg-gradient-success shadow-success text-center border-radius-xl mt-n4 position-absolute">
                                <i class="material-icons opacity-10">person</i>
                            </div>
                            <div class="text-end pt-1">
                                <p class="text-sm mb-0 text-capitalize">Participants</p>
                                <h4 class="mb-0">{{ $participants->count() }}</h4>
                            </div>
                        </div>
                        <hr class="dark horizontal my-0">
                        <div class="card-footer p-3">
                            <p class="mb-0">Registered participants</p>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-sm-6">
                    <div class="card">
                        <div class="card-header p-3 pt-2">
                            <div class="icon icon-lg icon-shape bg-gradient-info shadow-info text-center border-radius-xl mt-n4 position-absolute">
                                <i class="material-icons opacity-10">score</i>
                            </div>
                            <div class="text-end pt-1">
                                <p class="text-sm mb-0 text-capitalize">Attempts</p>
                                <h4 class="mb-0">{{ $attempts->count() }}</h4>
                            </div>
                        </div>
                        <hr class="dark horizontal my-0">
                        <div class="card-footer p-3">
                            <p class="mb-0"><span class="text-success text-sm font-weight-bolder">Attempts on challenges</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-4">
                <div class="col-lg-4 col-md-6 mt-4 mb-4">
                    <div class="card h-100 z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="chart-bars" class="chart-canvas" height="170"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">New Participants</h6>
                            <p class="text-sm ">New participants registering for the math challenge</p>
                            <hr class="dark horizontal">

                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm"> New data </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6 mt-4 mb-4">
                    <div class="card h-100 z-index-2  ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-success shadow-success border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="chart-line" class="chart-canvas" height="170"></canvas>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">New Attempts</h6>
                            <p class="text-sm "> New participants attempting the challenges. </p>
                            <hr class="dark horizontal">

                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm"> New data </p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 mt-4 mb-3">
                    <div class="card h-100 z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-dark shadow-dark border-radius-lg py-3 pe-1">
                                <div class="chart text-center">
                                    <img src="{{ asset('assets') }}/{{ $best_participant->image_path }}" height=150 alt="">

                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">Best participant</h6>
                            <p class="text-sm "><span class="fw-bold">Name: </span>{{ $best_participant->firstname }} {{ $best_participant->lastname }}</p>
                            <p class="text-sm "><span class="fw-bold">Average Score: </span>{{ round($best_participant->average_score, 2) }}</p>
                            <p><span class="fw-bold">School: </span>{{ $best_participant->school_name }}</p>
                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm">just updated</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-4">
                <div class="col-lg-8 col-md-6 mb-md-0 mb-4">
                    <div class="card h-100">
                        <div class="card-header pb-0">
                            <div class="row">
                                <div class="col-lg-6 col-7">
                                    <h6>School rankings</h6>
                                    <p class="text-sm mb-0">
                                        <i class="fa fa-check text-info" aria-hidden="true"></i>
                                        {{ $schools->count() }} registered schools</p>
                                </div>
                            </div>
                        </div>
                        <div class="card-body px-0 pb-2">
                            <div class="table-responsive">
                                <table class="table align-items-center mb-0">
                                    <thead>
                                        <tr>
                                            <th class="align-middle text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">

                                                ID</th>
                                            <th class="align-middle text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">

                                                NAME</th>
                                            <th class="align-middle text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">

                                                DISTRICT</th>
                                            <th class="align-middle text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">

                                                AVERAGE SCORE</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        @foreach($school_rankings as $school)

                                        <tr>
                                            <td class="align-middle text-center text-sm">
                                                <span class="text-xs font-weight-bold"> {{ $school->id }} </span>

                                            </td>

                                            <td class="align-middle text-left text-sm">
                                                <span class="text-xs font-weight-bold"> {{ $school->name }} </span>

                                            </td>

                                            <td class="align-middle text-center text-sm">
                                                <span class="text-xs font-weight-bold"> {{ $school->district }} </span>

                                            </td>

                                            <td class="align-middle">
                                                <div class="progress-wrapper mx-auto">

                                                    <div class="progress-info">
                                                        <div class="progress-percentage">
                                                            <span class="text-xs font-weight-bold">{{ round($school->average_score, 2) }}</span>

                                                        </div>
                                                    </div>
                                                    <div class="progress">
                                                        <div class="progress-bar bg-gradient-info" style="width:{{ round($school->average_score) }}%;" role="progressbar" aria-valuenow="{{ round($school->average_score) }}" aria-valuemin="0" aria-valuemax="100"></div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                        @endforeach

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card h-100">
                        <div class="card-header pb-0 p-3">
                            <div class="row">
                                <div class="col d-flex align-items-center">
                                    <h6 class="mb-0">Most correctly answered questions</h6>
                                </div>
                            </div>
                        </div>
                        <div class="card-body p-3 pb-0">
                            <ul class="list-group">
                                @foreach($most_correctly_answered_questions as $question)
                                <li class="list-group-item border-0 d-flex justify-content-between ps-0 mb-2 border-radius-lg">
                                    <div class="d-flex flex-column">
                                        <h6 class="mb-1 text-dark font-weight-bold text-sm">{{ $question->question }}</h6>
                                        <span class="text-xs text-success">Correct Answer: {{ $question->answer }}</span>
                                    </div>
                                    <div class="d-flex align-items-center text-sm">
                                        {{ $question->count }} times
                                    </div>
                                </li>
                                @endforeach
                            </ul>
                        </div>
                    </div>
                </div>

            </div>

            <div class="row mb-4 mt-5">
                <div class="col-lg-4 mt-4 mb-3">
                    <div class="card h-100 z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-dark shadow-dark border-radius-lg py-3 pe-1">
                                <div class="chart text-center">
                                    <img src="{{ asset('assets') }}/{{ $participant_percentage_repetition["participant"]->image_path }}" height=150 alt="">
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-3 ">Question repetition statistics</h6>
                            <span class="text-sm "><span class="fw-bold">Name: </span>{{ $participant_percentage_repetition["participant"]->firstname }} {{ $participant_percentage_repetition["participant"]->lastname }}</span>
                            <span class="text-sm ms-5"><span class="fw-bold">repetition: </span>{{ $participant_percentage_repetition["repetition"] }}%</span>

                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm">just updated</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 mt-4 mb-3">
                    <div class="card h-100 z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-dark shadow-dark border-radius-lg py-3 pe-1">
                                <div class="chart text-center">
                                    <h4 class="text-light">{{ $challenges_worst_schools["challenge"]->challenge_name }}</h4>

                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-3 ">Worst performing schools</h6>
                            <table class="borderless w-100">
                                <thead>
                                    <tr>
                                        <td>NAME</td>
                                        <td>AVG SCORE</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    @foreach($challenges_worst_schools["schools"] as $school)
                                    <tr>
                                        <td><span class="text-sm">{{ $school->name }}</span></td>
                                        <td><span class="text-sm">{{ round($school->counts) }}</span></td>
                                    </tr>
                                        
                                    @endforeach

                                </tbody>
                            </table>
                            <hr class="dark horizontal">
                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm">just updated</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 mt-4 mb-3">
                    <div class="card h-100 z-index-2 ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-dark shadow-dark border-radius-lg py-3 pe-1">
                                <div class="chart text-center">
                                    <h4 class="text-light">Participants with incomplete challenges</h4>
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <table class="borderless w-100">
                                <thead>
                                    <tr>
                                        <td class="fw-bold">participant</td>
                                        <td class="fw-bold">Date of attempt</td>

                                    </tr>
                                </thead>
                                <tbody>
                                @foreach($participants_with_incomplete_challenges as $participant)
                                    <tr>
                                        <td>{{ $participant->firstname }} {{ $participant->lastname }}</td>
                                        <td>{{ $participant->created_at }}</td>
                                    </tr>
                                @endforeach

                                </tbody>
                            </table>
                            <hr class="dark horizontal">
                            <div class="d-flex">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm">just updated</p>
                            </div>
                        </div>
                    </div>
                </div>


            </div>

            <div class="row mb-4">
                <div class="col-12 mt-5">
                    <div class="card h-100 z-index-2  ">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-info shadow-success border-radius-lg py-3 pe-1">
                                <div class="chart">
                                    <canvas id="best-schools-participants" class="chart-canvas" height="300"></canvas>

                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-0 ">Best schools and participants</h6>
                            <p class="text-sm "> This shows the performance of participants and schools over time. </p>
                            <hr class="dark horizontal">

                            <div class="d-flex ">
                                <i class="material-icons text-sm my-auto me-1">schedule</i>
                                <p class="mb-0 text-sm"> New data </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="row mt-5">
                @if($challenge_two_best_performers)
                    
                    @foreach($challenge_two_best_performers as $challenge)
                        <div class="col-6 mb-xl-0 mb-4">
                            <div class="card mb-5">
                                <div class="card-header p-3 pt-2">
                                    <div class="text-start pt-1">
                                        <p class="text-sm mb-0 text-capitalize">BEST</p>
                                        <h4 class="mb-0">{{ $challenge["name"] }}</h4>
                                    </div>
                                    <div class="icon icon-lg icon-shape bg-gradient-dark shadow-dark text-center border-radius-xl mt-n4 position-absolute end-5 top-0">
                                        <i class="material-icons opacity-10">grade</i>

                                    </div>
                                    
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        @foreach($challenge["top_two_participants"] as $participant)
                                            <div class="col-6">
                                                <div class="card shadow-0">
                                                    <div class="card-body">
                                                        <img src="{{ asset('assets') }}/{{ $participant["image_path"] }}" width="100%" height="120" alt="">
                                                    </div>
                                                    <div class="card-footer">
                                                        <table class="borderless w-100">
                                                            <tr>
                                                                <td class="fw-bold">username</td>
                                                                <td>: {{ $participant["username"] }}</td>
                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold">firstname</td>
                                                                <td>: {{ $participant["firstname"] }}</td>

                                                            </tr>

                                                            <tr>
                                                                <td class="fw-bold">last name</td>
                                                                <td>: {{ $participant["lastname"] }}</td>

                                                            </tr>
                                                            <tr>
                                                                <td class="fw-bold">avg. score</td>
                                                                <td>: {{ $participant["score"] }}</td>

                                                            </tr>

                                                        </table>
                                                    </div>

                                                </div>
                                            </div>                                        
                                        @endforeach

                                    </div>
                                </div>
                                <hr class="dark horizontal my-0">
                                <div class="card-footer p-3">
                                    <p class="mb-0">started on <b>{{ $challenge["opening_date"] }}</b> and closed on <b>{{ $challenge["closing_date"] }}</b></p>
                                </div>

                            </div>
                        </div>
                    @endforeach
                        
                @else
                    <div class="col-4">
                        <p>There are currently no closed challenges</p>
                    </div>
                @endif

                </div>



            </div>
            {{-- <x-footers.auth></x-footers.auth> --}}
        </div>
    </main>
    <x-plugins></x-plugins>
    </div>
    @push('js')
    <script src="{{ asset('assets') }}/js/plugins/chartjs.min.js"></script>
    <script>
        var ctx = document.getElementById("chart-bars").getContext("2d");

        new Chart(ctx, {
            type: "bar"
            , data: {
                labels: ["S", "M", "T", "W", "T", "F", "S"]
                , datasets: [{
                    label: "participants"
                    , tension: 0.4
                    , borderWidth: 0
                    , borderRadius: 4
                    , borderSkipped: false
                    , backgroundColor: "rgba(255, 255, 255, .8)"
                    , data: @json($weekly_participants)
                    , maxBarThickness: 6
                }, ]
            , }
            , options: {
                responsive: true
                , maintainAspectRatio: false
                , plugins: {
                    legend: {
                        display: false
                    , }
                }
                , interaction: {
                    intersect: false
                    , mode: 'index'
                , }
                , scales: {
                    y: {
                        grid: {
                            drawBorder: false
                            , display: true
                            , drawOnChartArea: true
                            , drawTicks: false
                            , borderDash: [5, 5]
                            , color: 'rgba(255, 255, 255, .2)'
                        }
                        , ticks: {
                            suggestedMin: 0
                            , suggestedMax: 500
                            , beginAtZero: true
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                            , color: "#fff"
                        }
                    , }
                    , x: {
                        grid: {
                            drawBorder: false
                            , display: true
                            , drawOnChartArea: true
                            , drawTicks: false
                            , borderDash: [5, 5]
                            , color: 'rgba(255, 255, 255, .2)'
                        }
                        , ticks: {
                            display: true
                            , color: '#f8f9fa'
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                        , }
                    }
                , }
            , }
        , });


        var ctx2 = document.getElementById("chart-line").getContext("2d");

        new Chart(ctx2, {
            type: "line"
            , data: {
                labels: ["S", "M", "T", "W", "T", "F", "S"]
                , datasets: [{
                    label: "Attempts"
                    , tension: 0.5
                    , borderWidth: 0
                    , pointRadius: 5
                    , pointBackgroundColor: "rgba(255, 255, 255, .8)"
                    , pointBorderColor: "transparent"
                    , borderColor: "rgba(255, 255, 255, .8)"
                    , borderColor: "rgba(255, 255, 255, .8)"
                    , borderWidth: 4
                    , backgroundColor: "transparent"
                    , fill: true
                    , data: @json($weekly_attempts)
                    , maxBarThickness: 6

                }]
            , }
            , options: {
                responsive: true
                , maintainAspectRatio: false
                , plugins: {
                    legend: {
                        display: false
                    , }
                }
                , interaction: {
                    intersect: false
                    , mode: 'index'
                , }
                , scales: {
                    y: {
                        grid: {
                            drawBorder: false
                            , display: true
                            , drawOnChartArea: true
                            , drawTicks: false
                            , borderDash: [5, 5]
                            , color: 'rgba(255, 255, 255, .2)'
                        }
                        , ticks: {
                            display: true
                            , color: '#f8f9fa'
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                        , }
                    }
                    , x: {
                        grid: {
                            drawBorder: false
                            , display: false
                            , drawOnChartArea: false
                            , drawTicks: false
                            , borderDash: [5, 5]
                        }
                        , ticks: {
                            display: true
                            , color: '#f8f9fa'
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                        , }
                    }
                , }
            , }
        , });

        var ctx3 = document.getElementById("best-schools-participants").getContext("2d");

        new Chart(ctx3, {
            type: "line"
            , data: {
                labels: ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sept", "oct", "nov", "dec"]
                , datasets: [
                @foreach($school_performance_over_time as $index => $performance)
                    {
                        label: "{{ $performance["name"] }}"
                        , tension: 0
                        , borderWidth: 0
                        , pointRadius: 5
                        , pointBackgroundColor: "{{ $colors[$index] }}"
                        , pointBorderColor: "transparent"
                        , borderColor: "{{ $colors[$index] }}"
                        , borderWidth: 4
                        , backgroundColor: "transparent"
                        , fill: true
                        , data: @json($performance["scores"])
                        , maxBarThickness: 6
                        , tension: .5

                    },
                @endforeach]

            , }
            , options: {
                responsive: true
                , maintainAspectRatio: false
                , plugins: {
                    legend: {
                        display: false
                    , }
                }
                , interaction: {
                    intersect: false
                    , mode: 'index'
                , }
                , scales: {
                    y: {
                        grid: {
                            drawBorder: false
                            , display: true
                            , drawOnChartArea: true
                            , drawTicks: false
                            , borderDash: [5, 5]
                            , color: 'rgba(255, 255, 255, .2)'
                        }
                        , ticks: {
                            display: true
                            , color: '#f8f9fa'
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                        , }
                    }
                    , x: {
                        grid: {
                            drawBorder: false
                            , display: false
                            , drawOnChartArea: false
                            , drawTicks: false
                            , borderDash: [5, 5]
                        }
                        , ticks: {
                            display: true
                            , color: '#f8f9fa'
                            , padding: 10
                            , font: {
                                size: 14
                                , weight: 300
                                , family: "Roboto"
                                , style: 'normal'
                                , lineHeight: 2
                            }
                        , }
                    }
                , }
            , }
        , });

    </script>
    @endpush
</x-layout>
