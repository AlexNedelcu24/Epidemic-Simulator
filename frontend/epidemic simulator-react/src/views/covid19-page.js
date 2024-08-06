import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'

import axios from '../api/axios';

import './covid19-page.css'


const PAGE_URL = '/page';

const Covid19Page = (props) => {

  const [videoLoaded, setVideoLoaded] = useState(false);

  const handleVideoLoad = () => {
    setVideoLoaded(true);
  }

  const [pageData, setPageData] = useState({ title: '', text: '', link: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.post(PAGE_URL, { id: 2 });
        setPageData(response.data);
        setLoading(false);
      } catch (error) {
        setError('Failed to fetch data');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }


  return (
    <div className="covid19-page-container">
      <Helmet>
        <title>Covid19Page - Epidemic Simulator</title>
        <meta property="og:title" content="Epidemic Simulator" />
      </Helmet>
      <div className="covid19-page-container1">
        <h1 className="covid19-page-text log">{pageData.title}</h1>
        <span className="covid19-page-text1">
          <span>
              {pageData.text}
          </span>
          
          <br></br>
        </span>

        {!videoLoaded && (
          <img 
            className="the-spanish-flu-page-placeholder"
            onLoad={() => setVideoLoaded(true)}
          />
        )}

        <iframe
          src={pageData.link}
          onLoad={handleVideoLoad}
          style={{display: videoLoaded ? 'block' : 'none'}}
          allow="accelerometer, autoplay"
          allowFullScreen="true"
          className="covid19-page-iframe"
        ></iframe>
      </div>
    </div>
  )
}

export default Covid19Page
