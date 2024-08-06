import React, { useEffect, useState } from 'react'

import { Helmet } from 'react-helmet'

import './black-death-page.css'

import axios from '../api/axios';

const PAGE_URL = '/page';

const BlackDeathPage = (props) => {

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
        const response = await axios.post(PAGE_URL, { id: 3 });
        const data = response.data;
        //data.text = data.text.replace(/&quot;/g, '"').replace(/&apos;/g, "'");
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
    <div className="black-death-page-container">
      <Helmet>
        <title>BlackDeathPage - Epidemic Simulator</title>
        <meta
          property="og:title"
          content="BlackDeathPage - Epidemic Simulator"
        />
      </Helmet>
      <div className="black-death-page-container1">
        <h1 className="black-death-page-text log">{pageData.title}</h1>
        <span className="black-death-page-text1">
            {pageData.text}
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
          className="black-death-page-iframe"
        ></iframe>
      </div>
    </div>
  )
}

export default BlackDeathPage
