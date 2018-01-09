class ForgeController < ApplicationController
  def new
    @model = Model.new
  end

  def upload
    puts params[:midiFile].original_filename
    @tm = TrainingModel.new
    @tm.model = @model


    redirect_to '/forge'
  end

  def clean_filename(file_name)
    just_filename = File.basename(file_name)
    just_filename.sub(/[^\w\.\-]/,'_')
  end
end
