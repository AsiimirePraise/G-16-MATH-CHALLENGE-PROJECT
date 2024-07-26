<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <link rel="icon" type="image/png" href="{{ asset('assets') }}/img/logos/favicon.png">
        <title>
            HOME | MATH CHALLENGE
        </title>

    <link rel="stylesheet" href="{{ asset('assets') }}/home/bootstrap/css/bootstrap.min.css">
    <link id="pagestyle" href="{{ asset('assets') }}/css/material-dashboard.css?v=3.0.0" rel="stylesheet" />

    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Inter:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800&amp;display=swap">
</head>

<body>
    <nav class="navbar navbar-light navbar-expand-md sticky-top navbar-shrink py-2" id="mainNav">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center" href="/">
                <img src="{{ asset('assets') }}/img/logos/favicon.png" height="42" alt="">

            </a>
            <div class="collapse navbar-collapse d-flex align-items-center" id="navcol-1">
                <ul class="navbar-nav mx-auto">
                </ul><span class="" role="button" href="signup.html">Are you an admin? <a href="{{ route('login') }}">Login</a></span>

            </div>
        </div>
    </nav>
    <header class="bg-primary-gradient">
        <div class="container pt-3 pt-xl-5">
            <div class="row pt-4">
                <div class="col-md-8 col-xl-6 text-center text-md-start mx-auto">
                    <div class="text-center">
                        <p class="fw-bold text-primary mb-2">Math challenge is voted #1 Countrywide</p>
                        <h1 class="fw-bold">The best platform for our next generation of geniuses</h1>
                    </div>
                </div>
                <div class="col-12 col-lg-10 mx-auto">
                    <div class="position-relative" style="display: flex;flex-wrap: wrap;justify-content: flex-end;">
                        <div style="position: relative;flex: 0 0 45%;transform: translate3d(-15%, 35%, 0);"><img class="img-fluid" data-bss-parallax="" data-bss-parallax-speed="0.8" src="{{ asset('assets') }}/home/img/hero/cover-1.jpg"></div>

                        <div style="position: relative;flex: 0 0 45%;transform: translate3d(-5%, 20%, 0);"><img class="img-fluid" data-bss-parallax="" data-bss-parallax-speed="0.4" height="200" src="{{ asset('assets') }}/home/img/hero/cover-5.jpg"></div>


                        <div style="position: relative;flex: 0 0 60%;transform: translate3d(0, 0%, 0);"><img class="img-fluid" data-bss-parallax="" data-bss-parallax-speed="0.25" src="{{ asset('assets') }}/home/img/hero/cover-4.jpg"></div>


                    </div>
                </div>
            </div>
        </div>
    </header>
    <section class="py-5">
        <div class="row row-cols-1 row-cols-md-2 mx-auto" style="max-width: 900px;">
            <div class="col mb-5"><img class="rounded border-rounded-lg img-fluid shadow" src="{{ asset('assets') }}/home/img/hero/cover-2.jpg"></div>

            <div class="col d-md-flex align-items-md-end align-items-lg-center mb-5">
                <div>
                    <h5 class="fw-bold">Our mission&nbsp;</h5>
                    <p class="text-muted mb-4">Our mission is to inspire a love for mathematics in primary school children through an engaging and challenging competition platform, promoting learning, critical thinking, and academic excellence.</p>

                </div>
            </div>
        </div>
    </section>

    <section class="py-5">
        <div class="row row-cols-1 row-cols-md-2 mx-auto" style="max-width: 900px;">
            <div class="col order-md-last mb-5"><img class="rounded img-fluid shadow" src="{{ asset('assets') }}/{{ $best_participant->image_path }}"></div>

            <div class="col d-md-flex align-items-md-end align-items-lg-center mb-5">
                <div>
                    <h5 class="fw-bold">Meet {{ $best_participant->firstname }} {{ $best_participant->lastname }}&nbsp;</h5>

                    <p class="text-muted mb-4">Meet our all time number 1 participant. {{ $best_participant->firstname }} is from {{ $best_participant->school_name }} and has been hitting on all cylinders. he has an average score of <span class="bg-success-light p-1"><strong>{{ round($best_participant->average_score, 1) }}%</strong></span></p>





                </div>
            </div>
        </div>

    </section>

    <section>
        <div class="container py-5">
            <div class="mx-auto" style="max-width: 900px;">
                <h2 class="text-secondary text-center mb-5">Check our stats</h2>
                <div class="row row-cols-1 row-cols-md-2 d-flex justify-content-center">
                    <div class="col mb-4">
                        <div class="card bg-primary-light">

                            <div class="card-body text-center px-4 py-5 px-md-5">
                                <div class="rounded-pill bg-primary p-3 text-light mx-auto mb-3" style="width: 64px">{{ $schools->count() }}</div>

                                <p class="fw-bold text-primary card-text mb-2">Registered Schools</p>
                                <h5 class="fw-bold card-title mb-3">Join us and be&nbsp;part of something big</h5>
                            </div>
                        </div>
                    </div>
                    <div class="col mb-4">
                        <div class="card bg-secondary-light">

                            <div class="card-body text-center px-4 py-5 px-md-5">
                                <div class="rounded-pill bg-secondary p-3 text-light mx-auto mb-3" style="width: 64px">{{ $challenges->count() }}</div>

                                <p class="fw-bold text-seondary card-text mb-2">Challenges hosted</p>
                                <h5 class="fw-bold card-title mb-3">Join us and be&nbsp;part of something big</h5>
                            </div>
                        </div>
                    </div>

                    <div class="col mb-4">
                        <div class="card bg-info-light">

                            <div class="card-body text-center px-4 py-5 px-md-5">
                                <div class="rounded-pill bg-info p-3 text-light mx-auto mb-3" style="width: 64px">{{ $attempts->count() }}</div>

                                <p class="fw-bold text-info card-text mb-2">attempts made</p>
                                <h5 class="fw-bold card-title mb-3">Join us and be&nbsp;part of something big</h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>


    <section class="py-5">
        <div class="container text-center py-5">
            <p class="mb-4" style="font-size: 1.6rem;">Attempted by <span class="bg-success-light p-1"><strong>{{ $participants->count() }}+</strong></span>&nbsp;students accross the country.</p>

            <span class="muted">#checkout the school performances over the years</span>
            <div class="row mb-4">
                <div class="col-12 mt-5">
                    <div class="card h-100 z-index-2 shadow-lg">
                        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
                            <div class="bg-gradient-info shadow-info border-radius-lg py-3 pe-1 shadow-sm">
                                <div class="chart">

                                    <canvas id="best-schools-participants" class="chart-canvas" height="300"></canvas>

                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <p class="" style="font-size: 1.4rem;"> This shows the performance of participants and schools over time. </p>

                            <hr class="dark horizontal">
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </section>
    <section class="py-5">
        <div class="container py-5">

            <div class="row row-cols-1 row-cols-md-2 mx-auto" style="max-width: 900px;">
                <div class="col mb-5 ps-5">
                    <table class="borderless w-100 bg-info-light">
                        <thead>
                            <tr>
                                <td class="fw-bold">RANK</td>
                                <td class="fw-bold">SCHOOL</td>
                                <td class="fw-bold">AVG SCORE</td>
                            </tr>
                        </thead>
                        <tbody class="pt-4">
                            @foreach($school_rankings as $count=>$school)

                            <tr>
                                <td class="ps-2">#{{ $count + 1 }}</td>
                                <td>{{ $school->name }}</td>
                                <td>{{ round($school->average_score, 1) }}</td>
                            </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
                <div class="col d-md-flex align-items-md-end order-first align-items-lg-center mb-5">
                    <div>
                        <h5 class="fw-bold">How is your school ranking&nbsp;</h5>
                        <p class="text-muted mb-4">Check how your school is ranking country wide. and have a chance to up your school ranks</p>
                    </div>
                </div>
            </div>
        </div>
    </section>


    <section class="py-5">
                    <div class="row mb-4 mb-lg-5">
                    <div class="col-md-8 col-xl-6 text-center mx-auto">
                        <h3 class="fw-bold">{{ Str::upper($challenges_worst_schools["challenge"]->challenge_name) }} CHALLENGE</h3>

                        <p>See how the schools are faring in the {{ Str::lower($challenges_worst_schools["challenge"]->challenge_name) }} challenge. don't get discouraged you can uprank your team anyday from our client portal</p>

                    </div>
                </div>
    </section>

    <section>
                                    <div class="row row-cols-1 row-cols-md-2 mx-auto" style="max-width: 900px;">
                                        <div class="col mb-5">
                                            <table class="borderless w-100 bg-primary-light">
                                                <thead>
                                                    <tr>
                                                        <td class="fw-bold">RANK</td>
                                                        <td class="fw-bold">SCHOOL</td>
                                                        <td class="fw-bold">AVG SCORE</td>
                                                    </tr>
                                                </thead>
                                                <tbody class="pt-4">
                                                    @foreach($challenges_worst_schools["schools"] as $count=>$school)


                                                    <tr>
                                                        <td class="ps-2">#{{ $challenges_worst_schools["schools"]->count() - $count }}</td>

                                                        <td>{{ $school->name }}</td>
                                                        <td>{{ round($school->counts, 1) }}</td>
                                                    </tr>
                                                    @endforeach
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="col d-md-flex align-items-md-end align-items-lg-center mb-5 ps-5">
                                            <div>
                                                <h5 class="fw-bold">How is your school ranking in {{ Str::lower($challenges_worst_schools["challenge"]->challenge_name) }}&nbsp;</h5>

                                                <p class="text-muted mb-4">Check how your school is ranking in the {{ Str::lower($challenges_worst_schools["challenge"]->challenge_name) }} challenge. and have a chance to up your school ranks</p>

                                            </div>
                                        </div>
                                    </div>


    </section>

        <section class="py-5">
            <div class="container py-5">
                <div class="row mb-5">
                    <div class="col-md-8 col-xl-6 text-center mx-auto">
                        <h2 class="fw-bold">Our geniuses</h2>
                        <p class="text-muted">Meet our best participants from all the challenges that we have hosted on math challenge system.</p>
                    </div>
                </div>
                

                <div class="row row-cols-1 row-cols-md-2 mx-auto" style="max-width: 900px;">
                @foreach($challenge_two_best_performers as $challenge)
                @foreach($challenge["top_two_participants"] as $count=>$participant)
                    <div class="col mb-4">
                        <div><a href="#"><img class="rounded img-fluid shadow w-100 fit-cover" src="{{ asset('assets') }}/{{ $participant["image_path"] }}" style="height: 250px;"></a>

                            <div class="py-4"><span class="badge rounded-pill bg-primary mb-2">{{ $challenge["name"] }}</span>
                                <h4 class="fw-bold">{{ Str::upper($participant["firstname"]) }} {{ Str::upper($participant["lastname"]) }}</h4>


                                <p class="text-muted">{{ $participant["firstname"] }} {{ $participant["lastname"] }} is among the best participant in {{ $challenge["name"] }}. the participant is from {{ $participant["school"] }}. and he has a whooping <span class="bg-info-light p-1"><strong>{{ $participant["score"] }}%</strong></span> average score</p>




                            </div>
                        </div>
                    </div>
                @endforeach
                @endforeach
                </div>
            </div>
        </section>

<section class="py-5 mt-5">
    <div class="container py-5">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <p class="fw-bold text-success mb-2">Testimonials</p>
                <h2 class="fw-bold"><strong>What People Say About Us</strong></h2>
                <p class="text-muted">Here's what participants and educators have to say about Math Challenge 2024.</p>
            </div>
        </div>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 d-sm-flex justify-content-sm-center">
            <div class="col mb-4">
                <div class="d-flex flex-column align-items-center align-items-sm-start">
                    <p class="bg-light border rounded border-light p-4">"Math Challenge 2024 has been an incredible experience for our students. It has sparked their interest in mathematics and motivated them to excel."</p>
                    <div class="d-flex"><img class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="{{ asset('assets') }}/home/img/team/avatar3.jpg">

                        <div>
                            <p class="fw-bold text-primary mb-0">Sarah Johnson</p>
                            <p class="text-muted mb-0">School Principal</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col mb-4">
                <div class="d-flex flex-column align-items-center align-items-sm-start">
                    <p class="bg-light border rounded border-light p-4">"Participating in Math Challenge 2024 was so much fun! I loved solving the challenging questions and competing with my friends."</p>
                    <div class="d-flex"><img class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="{{ asset('assets') }}/home/img/team/avatar6.jpg">


                        <div>
                            <p class="fw-bold text-primary mb-0">Alex Martinez</p>
                            <p class="text-muted mb-0">Participant</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col mb-4">
                <div class="d-flex flex-column align-items-center align-items-sm-start">
                    <p class="bg-light border rounded border-light p-4">"The Math Challenge 2024 platform is well-designed and easy to use. The random question selection keeps the competition fair and exciting."</p>
                    <div class="d-flex"><img class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="{{ asset('assets') }}/home/img/team/avatar5.jpg">


                        <div>
                            <p class="fw-bold text-primary mb-0">Emily Brown</p>
                            <p class="text-muted mb-0">Teacher</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


    <footer class="bg-primary-gradient">
        <div class="container py-4 py-lg-5">
            <hr>
            <div class="text-muted d-flex justify-content-between align-items-center pt-3">
                <p class="mb-0">Copyright © 2024 Math challenge</p>
                <ul class="list-inline mb-0">
                    <li class="list-inline-item"><svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-facebook">
                            <path d="M16 8.049c0-4.446-3.582-8.05-8-8.05C3.58 0-.002 3.603-.002 8.05c0 4.017 2.926 7.347 6.75 7.951v-5.625h-2.03V8.05H6.75V6.275c0-2.017 1.195-3.131 3.022-3.131.876 0 1.791.157 1.791.157v1.98h-1.009c-.993 0-1.303.621-1.303 1.258v1.51h2.218l-.354 2.326H9.25V16c3.824-.604 6.75-3.934 6.75-7.951z"></path>
                        </svg></li>
                    <li class="list-inline-item"><svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-twitter">
                            <path d="M5.026 15c6.038 0 9.341-5.003 9.341-9.334 0-.14 0-.282-.006-.422A6.685 6.685 0 0 0 16 3.542a6.658 6.658 0 0 1-1.889.518 3.301 3.301 0 0 0 1.447-1.817 6.533 6.533 0 0 1-2.087.793A3.286 3.286 0 0 0 7.875 6.03a9.325 9.325 0 0 1-6.767-3.429 3.289 3.289 0 0 0 1.018 4.382A3.323 3.323 0 0 1 .64 6.575v.045a3.288 3.288 0 0 0 2.632 3.218 3.203 3.203 0 0 1-.865.115 3.23 3.23 0 0 1-.614-.057 3.283 3.283 0 0 0 3.067 2.277A6.588 6.588 0 0 1 .78 13.58a6.32 6.32 0 0 1-.78-.045A9.344 9.344 0 0 0 5.026 15z"></path>
                        </svg></li>
                    <li class="list-inline-item"><svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-instagram">
                            <path d="M8 0C5.829 0 5.556.01 4.703.048 3.85.088 3.269.222 2.76.42a3.917 3.917 0 0 0-1.417.923A3.927 3.927 0 0 0 .42 2.76C.222 3.268.087 3.85.048 4.7.01 5.555 0 5.827 0 8.001c0 2.172.01 2.444.048 3.297.04.852.174 1.433.372 1.942.205.526.478.972.923 1.417.444.445.89.719 1.416.923.51.198 1.09.333 1.942.372C5.555 15.99 5.827 16 8 16s2.444-.01 3.298-.048c.851-.04 1.434-.174 1.943-.372a3.916 3.916 0 0 0 1.416-.923c.445-.445.718-.891.923-1.417.197-.509.332-1.09.372-1.942C15.99 10.445 16 10.173 16 8s-.01-2.445-.048-3.299c-.04-.851-.175-1.433-.372-1.941a3.926 3.926 0 0 0-.923-1.417A3.911 3.911 0 0 0 13.24.42c-.51-.198-1.092-.333-1.943-.372C10.443.01 10.172 0 7.998 0h.003zm-.717 1.442h.718c2.136 0 2.389.007 3.232.046.78.035 1.204.166 1.486.275.373.145.64.319.92.599.28.28.453.546.598.92.11.281.24.705.275 1.485.039.843.047 1.096.047 3.231s-.008 2.389-.047 3.232c-.035.78-.166 1.203-.275 1.485a2.47 2.47 0 0 1-.599.919c-.28.28-.546.453-.92.598-.28.11-.704.24-1.485.276-.843.038-1.096.047-3.232.047s-2.39-.009-3.233-.047c-.78-.036-1.203-.166-1.485-.276a2.478 2.478 0 0 1-.92-.598 2.48 2.48 0 0 1-.6-.92c-.109-.281-.24-.705-.275-1.485-.038-.843-.046-1.096-.046-3.233 0-2.136.008-2.388.046-3.231.036-.78.166-1.204.276-1.486.145-.373.319-.64.599-.92.28-.28.546-.453.92-.598.282-.11.705-.24 1.485-.276.738-.034 1.024-.044 2.515-.045v.002zm4.988 1.328a.96.96 0 1 0 0 1.92.96.96 0 0 0 0-1.92zm-4.27 1.122a4.109 4.109 0 1 0 0 8.217 4.109 4.109 0 0 0 0-8.217zm0 1.441a2.667 2.667 0 1 1 0 5.334 2.667 2.667 0 0 1 0-5.334z"></path>
                        </svg></li>
                </ul>
            </div>
        </div>
    </footer>
    <script src="{{ asset('assets') }}/home/bootstrap/js/bootstrap.min.js"></script>

    <script src="{{ asset('assets') }}/home/js/bs-init.js"></script>

    <script src="{{ asset('assets') }}/home/js/bold-and-bright.js"></script>

    <script src="{{ asset('assets') }}/js/plugins/chartjs.min.js"></script>
    <script>
        var ctx3 = document.getElementById("best-schools-participants").getContext("2d");

        new Chart(ctx3, {
            type: "line"
            , data: {
                labels: ["2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024"]
                , datasets: [
                        @foreach($school_performance_over_time as $index => $performance) {
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

                        }
                        , @endforeach
                    ]

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


</body>

</html>
