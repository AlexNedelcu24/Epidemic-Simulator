import React, { useState } from 'react'

import { Helmet } from 'react-helmet'
import { useHistory } from 'react-router-dom'

import './main-page.css'

const MainPage = (props) => {

  const history = useHistory()
  const [isExpanded, setIsExpanded] = useState(false);

  const handleLogout = () => {
    // Logica de logout
    console.log('Logout');
    history.push('/')
  };

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };




  const handleSpanishFluButton = () => {
    // logica

    history.push('/the-spanish-flu-page')
  }

  const handleCovid19Button = () => {
    // logica

    history.push('/covid19-page')
  }

  const handleBlackDeathButton = () => {
    // logica

    history.push('/black-death-page')
  }

  const handleThirdPlagueButton = () => {
    // logica

    history.push('/third-pague-pandemic')
  }

  const handle8990Button = () => {
    // logica

    history.push('/the1889-1890pandemic')
  }

  const handleCholeraButton = () => {
    // logica

    history.push('/the-cholera-pandemic')
  }

  const handleSmallpoxButton = () => {
    // logica

    history.push('/the-smallpox')
  }

  const handleTyphusButton = () => {
    // logica

    history.push('/the-typhus')
  }

  const handleNewPandemicButton = () => {
    // logica

    history.push('/new-pandemic-page')
  }

  const handleLoadPandemicButton = () => {
    // logica

    history.push('/load-an-existing-pandemic-page')
  }

  return (
    <div className="main-page-container">
      <Helmet>
        <title>MainPage - Epidemic Simulator</title>
        <meta property="og:title" content="Epidemic Simulator" />
      </Helmet>
      <div className={`logout-container ${isExpanded ? 'expanded' : ''}`} onClick={toggleExpand}>
        {isExpanded && (
          <button className="logout-button" onClick={handleLogout}>
            Logout
          </button>
        )}
      </div>
      <div className="main-page-container01">
        <span className="main-page-text wisetext">
          &quot;If anything kills over 10 million people in the next few
          decades, it’s most likely to be a highly infectious virus rather than
          a war. Not missiles, but microbes. We’ve actually invested very little
          in a system to stop an epidemic. We’re not ready for the next
          epidemic.&quot;
        </span>
        <span className="main-page-text1 billg">Bill Gates</span>
      </div>
      <div className="main-page-container02">
        <div className="main-page-container03">
          <h1 className="Heading">Simulate a pandemic</h1>
          <div className="main-page-container04">
            <button type="button" className="main-page-button button" onClick={handleNewPandemicButton}>
              Prepare simulation
            </button>
            <button type="button" className="main-page-button1 button" onClick={handleLoadPandemicButton}>
              Results
            </button>
          </div>
        </div>
      </div>
      <div className="main-page-container05">
        <div className="main-page-container06">
          <div className="main-page-container07">
            <h1 className="main-page-text3">The Spanish Flu</h1>
          </div>
          <button type="button" className="main-page-button2 button" onClick={handleSpanishFluButton}>
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container08">
        <div className="main-page-container09">
          <div className="main-page-container10">
            <h1 className="main-page-text4">Covid-19 pandemic</h1>
          </div>
          <button type="button" className="main-page-button3 button" onClick={handleCovid19Button}>
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container11">
        <div className="main-page-container12">
          <div className="main-page-container13">
            <h1 className="main-page-text5">Black Death</h1>
          </div>
          <button type="button" className="main-page-button4 button" onClick={handleBlackDeathButton}>
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container14">
        <div className="main-page-container15">
          <div className="main-page-container16">
            <h1 className="main-page-text6">Third plague pandemic</h1>
          </div>
          <button type="button" className="main-page-button5 button" onClick={handleThirdPlagueButton}>
            See more
          </button>
        </div>
      </div>

      <div className="main-page-container17">
        <div className="main-page-container18">
          <div className="main-page-container19">
            <h1 className="main-page-text07">The 1889–1890 pandemic</h1>
          </div>
          <button type="button" className="main-page-button06 button" onClick={handle8990Button}> 
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container20">
        <div className="main-page-container21">
          <div className="main-page-container22">
            <h1 className="main-page-text08">The cholera pandemic</h1>
          </div>
          <button type="button" className="main-page-button07 button" onClick={handleCholeraButton}>
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container23">
        <div className="main-page-container24">
          <div className="main-page-container25">
            <h1 className="main-page-text09">The history of smallpox</h1>
          </div>
          <button type="button" className="main-page-button08 button" onClick={handleSmallpoxButton}>
            See more
          </button>
        </div>
      </div>
      <div className="main-page-container26">
        <div className="main-page-container27">
          <div className="main-page-container28">
            <h1 className="main-page-text10">The typhus epidemic</h1>
          </div>
          <button type="button" className="main-page-button09 button" onClick={handleTyphusButton}>
            See more
          </button>
        </div>
      </div>

    </div>
  )
}

export default MainPage
