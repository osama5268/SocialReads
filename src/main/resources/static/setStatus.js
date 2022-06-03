const btnAvailable = document.querySelector("#available");
const btnNotAvailable = document.querySelector("#notavailable"); 
console.log('/view/'+bookid+'/setStatus/'+status);

function setStatus(status){
    fetch('/view/'+bookid+'/setStatus/'+status)
    .then(()=>{console.log('Book status changed')})
    .catch(()=>{console.log('Error changing status of book')})
}

btnAvailable.addEventListener('click', function(e){
    setStatus(1);
})
btnNotAvailable.addEventListener('click', function(e){
    setStatus(0);
})