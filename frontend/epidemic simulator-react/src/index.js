import React from 'react'
import ReactDOM from 'react-dom'
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect,
} from 'react-router-dom'

import './style.css'
import Register from './views/register'
import SimulationPage from './views/simulation-page'
import TheSpanishFluPage from './views/the-spanish-flu-page'
import MainPage from './views/main-page'
import LoadAnExistingPandemicPage from './views/load-an-existing-pandemic-page'
import Login from './views/login'
import NewPandemicPage from './views/new-pandemic-page'
import Covid19Page from './views/covid19-page'
import NotFound from './views/not-found'
import ResultsPage from './views/results-page'
import ThirdPaguePandemic from './views/third-pague-pandemic'
import BlackDeathPage from './views/black-death-page'
import The18891890pandemic from './views/the1889-1890pandemic'
import TheCholeraPandemic from './views/the-cholera-pandemic'
import TheSmallpox from './views/the-smallpox'
import TheTyphus from './views/the-typhus'


import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const App = () => {
  return (
    <Router>
      <Switch>
        <Route component={Register} exact path="/register" />
        <Route component={SimulationPage} exact path="/simulation-page" />
        <Route
          component={TheSpanishFluPage}
          exact
          path="/the-spanish-flu-page"
        />
        <Route component={MainPage} exact path="/main-page" />
        <Route
          component={LoadAnExistingPandemicPage}
          exact
          path="/load-an-existing-pandemic-page"
        />
        <Route
          component={The18891890pandemic}
          exact
          path="/the1889-1890pandemic"
        />
        <Route component={Login} exact path="/" />
        <Route component={NewPandemicPage} exact path="/new-pandemic-page" />
        <Route component={Covid19Page} exact path="/covid19-page" />
        <Route component={ResultsPage} exact path="/results-page" />
        <Route
          component={ThirdPaguePandemic}
          exact
          path="/third-pague-pandemic"
        />
        <Route
          component={TheCholeraPandemic}
          exact
          path="/the-cholera-pandemic"
        />
        <Route component={TheTyphus} exact path="/the-typhus" />
        <Route component={TheSmallpox} exact path="/the-smallpox" />
        <Route component={BlackDeathPage} exact path="/black-death-page" />
        <Route component={NotFound} path="**" />
        <Redirect to="**" />
      </Switch>
      <ToastContainer />
    </Router>
  )
}

ReactDOM.render(<App />, document.getElementById('app'))
