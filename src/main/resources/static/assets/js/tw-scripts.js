

  const sidebar = document.getElementById("sidebar");
const mobileToggle = document.getElementById("mobileToggle");
const backdrop = document.getElementById("backdrop");
const toggleBtn = document.getElementById("toggleBtn");
const content = document.getElementById("content");

/* MOBILE */
mobileToggle?.addEventListener("click", () => {
  const open = sidebar.classList.toggle("mobile-show");
  backdrop.classList.toggle("show", open);
});

/* BACKDROP CLOSE */
backdrop.addEventListener("click", () => {
  sidebar.classList.remove("mobile-show");
  backdrop.classList.remove("show");
});

/* DESKTOP + TABLET TOGGLE */
toggleBtn?.addEventListener("click", () => {

  const w = window.innerWidth;

  if (w >= 992 && w < 1200) {
    sidebar.classList.toggle("expanded");
    content.classList.toggle("expanded");
  } else {
    sidebar.classList.toggle("collapsed");
    content.classList.toggle("collapsed");
  }

  const icon = toggleBtn.querySelector("i");
  icon.classList.toggle("fa-chevron-left");
  icon.classList.toggle("fa-chevron-right");
});

/* RESIZE RESET */
window.addEventListener("resize", () => {
  const w = window.innerWidth;

  if (w >= 1200) {
    sidebar.classList.remove("mobile-show", "expanded");
    backdrop.classList.remove("show");
  }

  if (w < 992) {
    sidebar.classList.remove("expanded", "collapsed");
    content.classList.remove("expanded", "collapsed");
  }
});

document.querySelectorAll('.menu a').forEach(link => {
  link.addEventListener('click', function(e) {
    const isToggle = this.classList.contains('menu-link');
    const parentItem = this.closest('.menu-item');
    if (isToggle) {
      e.preventDefault();
      document.querySelectorAll('.menu-item').forEach(item => {
        if (item !== parentItem) item.classList.remove('active');
      });
      parentItem.classList.toggle('active');
      return;
    }
    document.querySelectorAll('.menu a.active').forEach(a => {
      a.classList.remove('active');
    });
    this.classList.add('active');
    document.querySelectorAll('.menu-item').forEach(item => {
      item.classList.remove('active');
    });
    if (parentItem) {
      parentItem.classList.add('active');
    }
  });
});
document.addEventListener('DOMContentLoaded', function () {

    var videoPopup = document.getElementById('videoModal');
    var createPopup = document.getElementById('createProject');

    if (videoPopup) {
        new bootstrap.Modal(videoPopup).show();
    } 
    else if (createPopup) {
        new bootstrap.Modal(createPopup).show();
    }
});
// window.onload = function () {
//     new bootstrap.Modal(document.getElementById('videoModal')).show();   
// };

const mediaBox = document.getElementById("mediaBox");
  const iframe = document.getElementById("videoFrame");
  const thumb = document.getElementById("thumb");
  const playIcon = document.getElementById("playIcon");
  let isPlaying = false;
  
  mediaBox.addEventListener("click", function () {
    if (!isPlaying) {
      iframe.src = "https://www.youtube.com/embed/7bwzhOox21s?autoplay=1&rel=0";
      iframe.style.display = "block";
      thumb.style.display = "none";
      playIcon.style.display = "none";
      isPlaying = true;
      setTimeout(resetToThumbnail, 110000);
    }
  });
  function resetToThumbnail() {
    iframe.src = ""; 
    iframe.style.display = "none";
    thumb.style.display = "block";
    playIcon.style.display = "block";
    isPlaying = false;
  }

  